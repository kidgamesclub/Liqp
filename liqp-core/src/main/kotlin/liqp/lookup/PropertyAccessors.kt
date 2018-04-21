package liqp.lookup

import liqp.exceptions.VariableNotExistException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Function

/**
 * Class that assists with looking up properties in a container object.
 */
class PropertyAccessors
private constructor(
    val lookups: List<AccessorResolutionStrategy>,
    val cache: MutableMap<String, PropertyAccessor>) {

  /**
   * Wraps a value as a [PropertyAccessor] so we can resolve the child properties.
   * @param value The value that has children properties
   * @return A valid property container, or null if no property accessor could be created
   */
  fun getAccessor(instance: Any, propertyName: String): PropertyAccessor {
    val type = instance::class
    //
    // 1. First, look for cached accessor.  This should be cheap... the cost of the hash lookup
    //
    val key = "${type.qualifiedName}.$propertyName"
    return cache.getOrPut(key, {
      //
      // 1. Look in the synthetic methods.  These can be overridden at runtime, so we're
      //    looking for the first non-null instance
      //
      for (lookup in lookups) {
        val lookupFn = lookup.getAccessor(instance, propertyName)
        if (lookupFn != null) {
          cache[key] = lookupFn
          return lookupFn
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
      return when (instance) {
        is HasProperties -> propertyAccessor<HasProperties> { i -> i.getProperty(propertyName) }
        is Map<*, *> -> propertyAccessor<Map<*, *>> { i -> i[propertyName] }
        else -> {
          return findField(type.java, propertyName)
              ?: findGetter(type.java, propertyName)
              ?: findNoArgMethod(type.java, propertyName)
              ?: nullAccessor
        }
      }
    })
  }

  constructor(vararg vlookups: AccessorResolutionStrategy) : this(
      listOf(*vlookups),
      mutableMapOf())

  fun withAccessorStrategies(vararg strategy: AccessorResolutionStrategy): PropertyAccessors {
    return PropertyAccessors(listOf(*strategy, *lookups.toTypedArray()), cache)
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
    fun <T> findField(type: Class<T>, name: String): PropertyAccessor? {
      return type.declaredFields
          .filter { it.name == name && Modifier.isPublic(it.modifiers) }
          .map { field ->
            field.isAccessible = true
            PropertyAccessors.of(field) }
          .firstOrNull()
    }

    @JvmStatic
    fun <T> findGetter(type: Class<T>, name: String): PropertyAccessor? {

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
            propertyAccessor<Any> { getter.invoke(it) }
          }
          .firstOrNull()
    }

    @JvmStatic
    fun <T> findNoArgMethod(type: Class<T>, name: String): PropertyAccessor? {
      return type.declaredMethods
          .filter {
            it.name == name
                && it.parameterCount == 0
                && Modifier.isPublic(it.modifiers)
                && it.returnType != Void.TYPE
          }
          .map { method ->
            method.isAccessible = true
            PropertyAccessors.of(method)
          }
          .firstOrNull()
    }

    @JvmStatic
    fun of(function: Function<Any, Any?>): PropertyAccessor {
      return object : PropertyAccessor {
        override fun get(instance: Any): Any? {
          return function.apply(instance)
        }
      }
    }

    @JvmStatic
    fun of(field: Field): PropertyAccessor {
      field.isAccessible = true
      return object : PropertyAccessor {
        override fun get(instance: Any): Any? {
          return field.get(instance)
        }
      }
    }

    @JvmStatic
    fun of(method: Method): PropertyAccessor {
      return object : PropertyAccessor {
        override fun get(instance: Any): Any? {
          return method.invoke(instance)
        }
      }
    }

    inline fun <reified T> propertyAccessor(crossinline lambda: Getter<T>): PropertyAccessor {
      return object : PropertyAccessor {
        override fun get(instance: Any): Any? {
          return lambda.invoke(instance as T)
        }
      }
    }

    @JvmStatic
    val nullAccessor = PropertyAccessors.propertyAccessor<Any>({ null })
  }

  fun propertyContainer(isStrictVariables:Boolean, instance:Any?):PropertyContainer {
    return when (instance) {
      null-> { _-> null}
      else-> { propertyName: String ->
        {
          val getter = getAccessor(instance, propertyName)
          when {
            isStrictVariables && getter.isNullAccessor() -> throw VariableNotExistException(propertyName)
            else -> getter.get(instance)
          }
        }
      }
    }
  }
}
