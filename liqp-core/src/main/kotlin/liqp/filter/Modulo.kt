package liqp.filter

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException

class Modulo : LFilter() {

  /*
     * plus(input, operand)
     *
     * modulus
     */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    val num = context.asNumber(value)?.toDouble() ?: 0.0
    val rhsObj = context.asNumber(params[0])?.toDouble() ?: 0.0

    if (rhsObj == 0.0) {
      throw LiquidRenderingException("Div by 0")
    }
    val value = num % rhsObj
    return if(context.isIntegral(value)) value.toLong() else value
  }
}
