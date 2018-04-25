package liqp.filter

import liqp.*
import liqp.context.LContext

class Default : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (params == null || params.size == 0) {
      return value
    }

    return if (value.isFalsy()) {
      params[0]
    } else value
  }
}
