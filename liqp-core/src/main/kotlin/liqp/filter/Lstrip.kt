package liqp.filter

import liqp.context.LContext

class Lstrip : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return {
      if (value is String) value.trimStart()
      else value
    }
  }
}
