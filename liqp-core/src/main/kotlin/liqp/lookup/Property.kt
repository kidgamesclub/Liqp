package liqp.lookup

import liqp.context.LContext
import liqp.exceptions.MissingVariableException

class Property(private val propertyName: String) : Indexable {

  var getter: Getter<Any>? = null

  override fun get(value: Any?, context: LContext): Any? {
    if (value == null && context.parseSettings.isStrictVariables) {
      throw MissingVariableException(propertyName)
    } else if (value == null) {
      return null
    }

    if (getter == null) {
      synchronized(this) {
        if (getter == null) {
          getter = context.getAccessor(value, propertyName)
        }
      }
    }

    val getter = getter!!
    if (context.parseSettings.isStrictVariables && getter.isNullAccessor()) {
      throw MissingVariableException(propertyName)
    }

    return getter.invoke(value)
  }

  override fun toString(): String {
    return ".$propertyName"
  }
}

