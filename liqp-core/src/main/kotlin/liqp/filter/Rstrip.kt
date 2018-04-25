package liqp.filter

import liqp.context.LContext

class Rstrip : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    return if (!super.isString(value)) {
      value
    } else super.asString(value).replace("\\s+$".toRegex(), "")
  }
}
