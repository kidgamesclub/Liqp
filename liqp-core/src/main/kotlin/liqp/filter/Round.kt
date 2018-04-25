package liqp.filter

import java.text.DecimalFormat
import liqp.context.LContext

class Round : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (!super.canBeDouble(value)) {
      return 0
    }

    val formatBuilder = StringBuilder("0")
    val number = super.asNumber(value!!).toDouble()
    var round: Long? = 0L

    if (params.size > 0 && super.canBeDouble(params[0])) {
      round = super.asNumber(params[0]).toLong()
    }

    if (round > 0) {
      formatBuilder.append(".")

      for (i in 0 until round) {
        formatBuilder.append("0")
      }
    }

    val formatter = DecimalFormat(formatBuilder.toString())

    return formatter.format(number)
  }
}
