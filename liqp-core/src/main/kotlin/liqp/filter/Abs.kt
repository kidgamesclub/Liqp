package liqp.filter

import liqp.context.LContext
import kotlin.math.absoluteValue

class Abs : LFilter() {
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    context.run {
       return asLong(value)?.absoluteValue ?:
        asDouble(value)?.absoluteValue ?: 0
    }
  }
}
