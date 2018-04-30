package liqp.filter

import liqp.context.LContext
import liqp.isFalsy

class Default : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return {
      if (value.isFalsy()) params[0]
      else value
    }
  }
}
