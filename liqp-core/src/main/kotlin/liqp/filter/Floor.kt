package liqp.filter

import liqp.context.LContext

class Floor : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return Math.floor(context.asDouble(value) ?: 0.0).toLong()
  }
}
