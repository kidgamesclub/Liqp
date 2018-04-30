package liqp.filter

import liqp.context.LContext

class Times : LFilter() {

  /**
   * times(input, operand)
   *
   * multiplication
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.mult(value, params[0])
  }
}
