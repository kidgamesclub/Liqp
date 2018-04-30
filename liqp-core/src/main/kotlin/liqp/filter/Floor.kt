package liqp.filter

import liqp.context.LContext

class Floor : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return Math.floor(context.asDouble(value) ?: 0.0).toLong()
  }
}
