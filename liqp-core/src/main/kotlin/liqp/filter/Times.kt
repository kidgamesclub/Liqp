package liqp.filter

import liqp.context.LContext

class Times : LFilter() {

  /**
   * times(input, operand)
   *
   * multiplication
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    val num = context.asNumber(value)?.toDouble() ?: 0.0
    val rhsObj = context.asNumber(params[0])?.toDouble() ?: 0.0

    return num * rhsObj
  }
}
