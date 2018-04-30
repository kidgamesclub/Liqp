package liqp.filter

import liqp.context.LContext

class Plus : LFilter() {

  /**
   * plus(input, operand)
   *
   * addition
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.add(value, params[0])
  }
}
