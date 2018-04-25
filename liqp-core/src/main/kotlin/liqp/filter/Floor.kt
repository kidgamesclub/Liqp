package liqp.filter

import liqp.context.LContext

class Floor : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    return if (!super.isNumber(value)) {
      value
    } else Math.floor(super.asNumber(value!!).toDouble()).toLong()
  }
}
