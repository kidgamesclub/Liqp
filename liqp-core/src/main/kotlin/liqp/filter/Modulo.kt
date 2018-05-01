package liqp.filter

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.isIntegral

class Modulo : LFilter() {

  /**
   * plus(input, operand)
   *
   * modulus
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val num = context.asNumber(value)?.toDouble() ?: 0.0
    val param1: Any? = params[0]

    val rhsObj = context.asNumber(params[0])?.toDouble() ?: 0.0

    if (rhsObj == 0.0) {
      throw LiquidRenderingException("Div by 0")
    }

    val mod = num % rhsObj
    return if (value.isIntegral() && param1.isIntegral()) mod.toLong() else mod
  }
}
