package liqp.filter

import liqp.context.LContext

class Floor : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return Math.floor(context.asDouble(value) ?: 0.0)
  }
}
