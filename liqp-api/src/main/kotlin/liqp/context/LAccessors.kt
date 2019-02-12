package liqp.context

import liqp.Getter

interface LAccessors {
  /**
   * Wraps a value as a [PropertyAccessor] so we can resolve the child properties.
   * @param value An example instance that has the child properties
   * @return A valid property container, or null if no property accessor could be created
   */
  fun getAccessor(lcontext: LContext, sample: Any, propertyName: String): Getter<Any>
}
