package liqp.lookup

import liqp.exceptions.MissingVariableException
import liqp.nodes.RenderContext

class Property(private val isStrictVariables:Boolean,
               private val propertyName: String) : Indexable {

  var getter: Getter<Any>? = null

  override fun get(value: Any?, context: RenderContext): Any? {
    if (value == null) {
      return null
    }

    if (getter == null) {
      synchronized(this) {
        if (getter == null) {
          getter = context.accessors.getAccessor(value, propertyName)
        }
      }
    }

    val getter= getter!!
    if (isStrictVariables && getter.isNullAccessor()) {
      throw MissingVariableException(propertyName)
    }

    return getter.invoke(value)
  }

  override fun toString(): String {
    return ".$propertyName"
  }
}

