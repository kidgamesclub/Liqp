package liqp.lookup

import liqp.PropertyContainer
import liqp.context.LAccessors
import liqp.exceptions.MissingVariableException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Function
import kotlin.reflect.jvm.jvmName

/**
 * Class that assists with looking up properties in a container object.
 */
data class PropertyAccessors
constructor(
    val lookups: List<AccessorResolutionStrategy>,
    val cache: MutableMap<String, Getter<Any>>,
    val typeGetCache: MutableMap<String, Method?>) : LAccessors {

  constructor(vararg vlookups: AccessorResolutionStrategy) : this(
      listOf(*vlookups),
      mutableMapOf(),
      mutableMapOf())

  /**
   * Wraps a value as a [PropertyAccessor] so we can resolve the child properties.
   * @param value The value that has children properties
   * @return A valid property container, or null if no property accessor could be created
   */
  override fun getAccessor(sample: Any, propertyName: String): Getter<Any> {
    val type = sample::class
    //
    // 1. First, look for cached accessor.  This should be cheap... the cost of the hash lookup
    //
    val key = "${type.jvmName}.$propertyName"
    return cache.getOrPut(key, cache@{
      //
      // 1. Look in the synthetic methods.  These can be overridden at runtime, so we're
      //    looking for the first non-null instance
      //
      for (lookup in lookups) {
        val lookupFn = lookup.getAccessor(sample, propertyName)
        if (lookupFn != null) {
          return@cache lookupFn
        }
      }

      //
      // 2. Fall back to known types
      // -- PropertyContainer
      // -- Map
      // -- Reflection (field)
      // -- Reflection (no-args methods)
      // -- Null
      //
      val accessor: Getter<Any> = when (sample) {
        is HasProperties -> { i -> (i as HasProperties).get(propertyName) }
        is Map<*, *> -> { i -> (i as Map<*, *>)[propertyName] }
        is Pair<*, *> -> ofPair(propertyName)
        is Function1<*, *> -> { i -> (i as Function1<String, Any?>)(propertyName) }
        else -> {
          findField(type.java, propertyName)
              ?: findGetter(type.java, propertyName)
              ?: findNoArgMethod(type.java, propertyName)
              ?: findGetMethod(type.java, propertyName)
              ?: nullAccessor
        }
      }
      return@cache accessor
    })
  }

  fun <T> findGetMethod(type: Class<T>, prop: String): Getter<Any>? {
    return typeGetCache.getOrPut(type.name, { findGetMethodForClass(type) })
        ?.let { method -> { instance -> method.invoke(instance, prop) } }
  }

  fun withAccessorStrategies(vararg strategy: AccessorResolutionStrategy): PropertyAccessors {
    return this.copy(lookups = listOf(*strategy, *lookups.toTypedArray()))
  }

  companion object {
    @JvmStatic
    val defaultInstance = PropertyAccessors()

    @JvmStatic
    fun newInstance(): PropertyAccessors {
      return PropertyAccessors(StringSizeAccessor(), FirstElementAccessor(),
          LastElementAccessor())
    }

    @JvmStatic
    fun <T> findField(type: Class<T>, name: String): Getter<Any>? {
      return type.declaredFields
          .filter { it.name == name && Modifier.isPublic(it.modifiers) }
          .map { field ->
            field.isAccessible = true
            PropertyAccessors.ofField(field)
          }
          .firstOrNull()
    }

    fun <T> findGetter(type: Class<T>, name: String): Getter<Any>? {
      return type.declaredMethods
          .filter {
            (it.name == "get${name.capitalize()}"
                || it.name == "is${name.capitalize()}")
                && it.returnType != Unit::class
                && it.returnType != Void::class
                && Modifier.isPublic(it.modifiers)
                && it.parameterCount == 0
          }
          .map { getter ->
            getter.isAccessible = true
            { t: Any -> getter.invoke(t) }
          }
          .firstOrNull()
    }

    @JvmStatic
    fun <T> findGetMethodForClass(type: Class<T>): Method? {
      return type.declaredMethods.firstOrNull {
        (it.name == "get"
            && it.returnType != Unit::class
            && it.returnType != Void::class
            && Modifier.isPublic(it.modifiers)
            && it.parameterCount == 1
            && it.parameterTypes[0] == String::class.java)
      }
    }

    @JvmStatic
    fun <T> findNoArgMethod(type: Class<T>, name: String): Getter<Any>? {
      return type.declaredMethods
          .filter {
            it.name == name
                && it.parameterCount == 0
                && Modifier.isPublic(it.modifiers)
                && it.returnType != Void.TYPE
          }
          .map { method ->
            method.isAccessible = true
            { t: Any -> method.invoke(t) }
          }
          .firstOrNull()
    }

    @JvmStatic
    fun <P, R> ofFunction(function: Function<P, R?>): Getter<P> {
      return { p: P -> function.apply(p) }
    }

    @JvmStatic
    fun ofField(field: Field): Getter<Any> {
      field.isAccessible = true
      return { instance -> field.get(instance) }
    }

    @JvmStatic
    fun ofMethod(method: Method): Getter<Any> {
      method.isAccessible = true
      return { instance -> method.invoke(instance) }
    }

    fun ofPair(prop: String): Getter<Any> {
      return { instance ->
        instance as Pair<String, Any?>
        when (instance.first) {
          prop -> instance.second
          else -> null
        }
      }
    }

    @JvmStatic
    val nullAccessor: Getter<Any> = { _: Any -> null }
  }

  fun propertyContainer(isStrictVariables: Boolean, instance: Any?): PropertyContainer {
    return when (instance) {
      null -> { _ -> null }
      else -> { propertyName: String ->
        {
          val getter = getAccessor(instance, propertyName)
          when {
            isStrictVariables && getter.isNullAccessor() -> throw MissingVariableException(propertyName)
            else -> getter.invoke(instance)
          }
        }
      }
    }
  }
}
