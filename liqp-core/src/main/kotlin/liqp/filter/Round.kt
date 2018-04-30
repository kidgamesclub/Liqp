package liqp.filter

import liqp.context.LContext
import java.math.BigDecimal
import java.math.RoundingMode

class Round : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    val digits = params[0, 0]
    val number = context.asDouble(value) ?: return value
    val rounded = BigDecimal(number)
        .setScale(digits, RoundingMode.HALF_UP)
        .toDouble()

    return rounded
  }
}
