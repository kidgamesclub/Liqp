package liqp.filter

import liqp.context.LContext

class Plus : LFilter() {

  /**
   * plus(input, operand)
   *
   * addition
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    var sum = value
    for (param in params) {
      sum = context.add(sum, param)
    }
    return sum
  }
}
