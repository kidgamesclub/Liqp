package liqp

import java.util.function.Function


interface PropertyGetter {
  fun getter(property: String): Getter<Any> = getterOrNull(property)
      ?: error("No property access for $property")

  fun getterOrNull(property: String): Getter<Any>?
}
typealias Getter<T> = (T) -> Any?
typealias GetterJvm<T> = Function<T, Any?>

@Suppress("UNCHECKED_CAST")
interface HasProperties : PropertyGetter {
  fun <T:Any> getValue(propName: String): T?

  override fun getterOrNull(property: String): Getter<Any>? {
    return { instance ->
      (instance as HasProperties).getValue(property)
    }
  }
}

@Suppress("UNCHECKED_CAST")
abstract class HasPropertiesJvm() : HasProperties {
  abstract override fun <T:Any> getValue(propName: String): T?
  override fun getter(property: String): Getter<Any> {
    return super.getter(property)
  }

  override fun getterOrNull(property: String): Getter<Any>? {
    return super.getterOrNull(property)
  }
}

