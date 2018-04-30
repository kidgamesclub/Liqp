package liqp.filter

import liqp.context.LContext

class DividedBy : LFilter() {

  /**
   * divided_by(input, operand)
   *
   * division
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.div(value, params[0])
  }
}
