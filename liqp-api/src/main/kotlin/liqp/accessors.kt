package liqp


interface PropertyGetter {
  fun getter(property: String): Getter<Any> = getterOrNull(property)
      ?: error("No property access for $property")

  fun getterOrNull(property: String): Getter<Any>?
}
typealias Getter<T> = (T) -> Any?

@Suppress("UNCHECKED_CAST")
interface HasProperties : PropertyGetter {
  fun <T:Any> getValue(propName: String): T?
  override fun getterOrNull(property: String): Getter<Any>? {
    return { instance ->
      (instance as HasProperties).getValue(property)
    }
  }
}

