package liqp.filter

import liqp.context.LContext
import java.math.BigDecimal
import java.math.RoundingMode

class Round : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {

    val digits = params[0, 0L].toInt()
    val number = context.asDouble(value) ?: return value
    val rounded = BigDecimal(number)
        .setScale(digits, RoundingMode.HALF_UP)
        .toDouble()

    return when(digits) {
      0-> rounded.toLong()
      else-> rounded
    }
  }
}
