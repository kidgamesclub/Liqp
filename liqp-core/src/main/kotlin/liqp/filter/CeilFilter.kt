package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * {{ 4 | ceil }} -> 4
 * {{ 4.3 | ceil }} -> 5
 */
class CeilFilter : LFilter() {
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      return Math.ceil(asDouble(value) ?: 0.0).toLong()
    }
  }
}
