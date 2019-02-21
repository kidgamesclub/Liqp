package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class FloorFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return Math.floor(context.asDouble(value) ?: 0.0).toLong()
  }
}
