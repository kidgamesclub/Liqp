package liqp.lookup

import liqp.context.LContext
import liqp.exceptions.MissingVariableException
import liqp.onMissingVariable

class Property(private val propertyName: String) : Indexable {

  var getter: Getter<Any>? = null

  override fun get(value: Any?, context: LContext): Any? {
    if (value == null) {
      return context.onMissingVariable(propertyName)
    }

    if (getter == null) {
      synchronized(this) {
        if (getter == null) {
          getter = context.getAccessor(value, propertyName)
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

