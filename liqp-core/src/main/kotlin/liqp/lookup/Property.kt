package liqp.lookup

import liqp.exceptions.VariableNotExistException
import liqp.nodes.RenderContext

class Property(val isStrictPropertyNames:Boolean, private val propertyName: String) : Indexable {

  var getter: PropertyAccessor? = null

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
    if (isStrictPropertyNames && getter.isNullAccessor()) {
      throw VariableNotExistException(propertyName)
    }

    return getter.get(value)
  }

  override fun toString(): String {
    return ".$propertyName"
  }
}

