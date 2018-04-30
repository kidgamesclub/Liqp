package liqp.filter

import liqp.context.LContext

class DividedBy : LFilter() {

  /**
   * divided_by(input, operand)
   *
   * division
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      val number = asNumber(value)?.toDouble() ?: 0.0
      val paramNum = asNumber(params[0])?.toDouble() ?: 0.0

      if (paramNum != 0.0) {
        return number / paramNum
      }
    }

    return value
  }
}
