package liqp.filter

import liqp.context.LContext

class Upcase : LFilter() {

  /**
   * upcase(input)
   *
   * convert a input string to UPCASE
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.asString(value)?.toUpperCase()
  }
}
