package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import kotlin.math.absoluteValue

class Abs : LFilter() {
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
       return asLong(value)?.absoluteValue ?:
        asDouble(value)?.absoluteValue ?: 0
    }
  }
}
