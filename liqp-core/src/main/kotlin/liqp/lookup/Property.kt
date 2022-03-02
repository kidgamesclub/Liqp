package liqp.lookup

import liqp.context.LContext
import liqp.exceptions.MissingVariableException
import liqp.onMissingVariable

class Property(private val propertyName: String) : Indexable {

  var getter: Getter<Any>? = null
  var getterKey: String? = null

  override fun get(value: Any?, context: LContext): Any? {
    if (value == null) {
      return context.onMissingVariable(propertyName)
    }

    val key = context.getAccessorKey(value, propertyName)

    if (getter == null || key != getterKey) {
      synchronized(this) {
        if (getter == null || key != getterKey) {
          getter = context.getAccessor(value, propertyName)
          getterKey = key
        }
      }
    }

    val getter = getter!!
    if (getter.isNullAccessor()) {
      return context.onMissingVariable(propertyName)
    }

    return getter.invoke(value)
  }

  override fun toString(): String {
    return ".$propertyName"
  }
}

