package liqp

import lang.exception.illegalArgument
import lang.suppress.Suppressions

interface PropertyGetter {
  fun getter(property: String): Getter<Any> = getterOrNull(property)
      ?: illegalArgument("No property access for $property")

  fun getterOrNull(property: String): Getter<Any>?
}
typealias Getter<T> = (T) -> Any?

@Suppress(Suppressions.UNCHECKED_CAST)
interface HasProperties : PropertyGetter {
  fun <T:Any> getValue(propName: String): T?
  override fun getterOrNull(property: String): Getter<Any>? {
    return { instance ->
      (instance as HasProperties).getValue(property)
    }
  }
}

