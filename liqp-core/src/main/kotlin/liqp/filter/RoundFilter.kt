package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToLong

class RoundFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {

    val p1:Number = params[0] ?: 0
    val digits = p1.toInt()
    val number = context.asDouble(value) ?: return 0L
    val rounded = BigDecimal(number)
        .setScale(digits, RoundingMode.HALF_UP)
        .toDouble()

    return when(digits) {
      0-> rounded.roundToLong()
      else-> rounded
    }
  }
}
