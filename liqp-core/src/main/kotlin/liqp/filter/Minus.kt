package liqp.filter

import liqp.context.LContext

class Minus : LFilter() {

  /**
   * plus(input, operand)
   *
   * subtraction
   */
  override fun onFilterAction(params: FilterParams, value: Any?,
                              chain: FilterChainPointer, context: LContext): Any? {
    return context.subtract(value, params[0])
  }
}
