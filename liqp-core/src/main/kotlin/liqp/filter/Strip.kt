package liqp.filter

import liqp.context.LContext

class Strip : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return {
      if (value is String) value.trim()
      else value
    }
  }
}
