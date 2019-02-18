package liqp.lookup

import lang.json.JsrObject
import lang.json.unbox
import lang.json.unboxAsAny
import liqp.Getter
import liqp.HasProperties
import liqp.PropertyGetter
import liqp.context.LAccessors
import liqp.context.LContext
import liqp.exceptions.MissingVariableException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Function
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

/**
 * Class that assists with looking up properties in a container object.
 */
data class PropertyAccessors(
    val lookups: Map<out Any, AccessorResolutionStrategy>,
    val cache: MutableMap<String, Getter<Any>>,
    val typeGetCache: MutableMap<String, Method?>) : LAccessors {

  constructor(vararg vlookups: AccessorResolutionStrategy) : this(
      vlookups.toList().toAccessorMap(),
      mutableMapOf(),
      mutableMapOf())

  /**
   * Wraps a value as a [PropertyAccessor] so we can resolve the child properties.
   * @param value The value that has children properties
   * @return A valid property container, or null if no property accessor could be created
   */
  override fun getAccessor(lcontext: LContext, sample: Any, propertyName: String): Getter<Any> {
    val type = sample::class
    //
    // 1. First, look for cached accessor.  This should be cheap... the cost of the hash lookup
    //
    val key = "${type.jvmName}.$propertyName"
    return cache.getOrPut(key) cache@{
      //
      // 1. Look in the synthetic methods.  These can be overridden at runtime, so we're
      //    looking for the first non-null instance
      //
      for (lookup in lookups.values) {
        val lookupFn = lookup.getAccessor(lcontext, sample, propertyName)
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
      @Suppress("unchecked_cast")
      val accessor: Getter<Any> = when (sample) {
        is PropertyGetter -> sample.getter(propertyName)
        is Getter<*> -> sample as Getter<Any>
        is JsrObject-> {json-> (json as JsrObject).get(propertyName)?.unboxAsAny()}
        is Map<*, *> -> { map -> (map as Map<*, *>)[propertyName] }
        is Pair<*, *> -> ofPair(propertyName)
        is Function1<*, *> -> { i -> (i as Function1<String, Any?>).invoke(propertyName) }
        else -> {
          findField(type.java, propertyName)
              ?: findGetter(type.java, propertyName)
              ?: type.findNoArgMethod(propertyName)
              ?: findGetMethod(type.java, propertyName)
              ?: NullGetter
        }
      }
      return@cache accessor
    }
  }

  private fun <T> findGetMethod(type: Class<T>, prop: String): Getter<Any>? {
    return typeGetCache.getOrPut(type.name) { findGetMethodForClass(type) }
        ?.let { method -> { instance -> method.invoke(instance, prop) } }
  }

  fun withAccessorStrategies(vararg strategy: AccessorResolutionStrategy): PropertyAccessors {
    return this.copy(lookups = lookups + strategy.toList().toAccessorMap())
  }

  companion object {
    @JvmStatic
    val DefaultPropertyAccessors = PropertyAccessors()

    private fun Iterable<AccessorResolutionStrategy>.toAccessorMap(): Map<Any, AccessorResolutionStrategy> =
        map { it::class to it }.toMap()

    @JvmStatic
    fun newInstance(): PropertyAccessors {
      return PropertyAccessors(StringSizeAccessor(), FirstElementAccessor(),
          LastElementAccessor())
    }

    @JvmStatic
    fun <T> findField(type: Class<T>, name: String): Getter<Any>? {
      return type.declaredFields.asSequence()
          .plus(type.fields)
          .distinct()
          .filter { it.name == name  }
          .map { field ->
            field.isAccessible = true
            field.toGetter()
          }
          .firstOrNull()
    }

    fun <T> findGetter(type: Class<T>, name: String): Getter<Any>? {
      return type.methods
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
    fun <T : Any> KClass<T>.findNoArgMethod(name: String): Getter<Any>? = let { type ->
      return type.java.declaredMethods
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
    fun <P, R> Function<P, R?>.toGetter(): Getter<P> = { p: P -> this.apply(p) }

    @JvmStatic
    fun Field.toGetter(): Getter<Any> = let { field ->
      field.isAccessible = true
      return { instance -> field.get(instance) }
    }

    @JvmStatic
    fun Method.toGetter(): Getter<Any> = let { method ->
      method.isAccessible = true
      return { instance -> method.invoke(instance) }
    }

    @Suppress("UNCHECKED_CAST")
    fun ofPair(prop: String): Getter<Any> {
      return { instance ->
        instance as Pair<String, Any?>
        when (instance.first) {
          prop -> instance.second
          else -> null
        }
      }
    }
  }

  fun propertyContainer(lcontext: LContext, instance: Any?): PropertyGetter {
    val type = if (instance != null) instance::class else null
    return when (instance) {
      null -> NullPropertyContainer
      else -> LContextPropertyContainer(lcontext, type)
    }
  }

  object NullPropertyContainer : PropertyGetter {
    override fun getterOrNull(property: String): Getter<Any>? = NullGetter
  }

  object NullGetter : Getter<Any> {
    override fun invoke(instance: Any): Any? = null
  }

  data class LContextPropertyContainer(val lcontext: LContext, val type: KClass<*>?) : PropertyGetter {
    override fun getterOrNull(property: String): Getter<Any>? = LContextGetter(lcontext, type, property)

    override fun toString(): String {
      return "Container for [${(type?.qualifiedName ?: "Unknown")}]"
    }
  }

  data class LContextGetter(val lcontext: LContext, val type: KClass<*>?, val propertyName: String) : Getter<Any> {
    override fun invoke(instance: Any): Any? {
      val getter = lcontext.getAccessor(instance, propertyName)
      return when {
        lcontext.renderSettings.isStrictVariables && getter.isNullAccessor() -> throw MissingVariableException(propertyName)
        else -> getter.invoke(instance)
      }
    }

    override fun toString(): String {
      return "Getter for [${(type?.qualifiedName ?: "Unknown")}].$propertyName"
    }
  }
}
