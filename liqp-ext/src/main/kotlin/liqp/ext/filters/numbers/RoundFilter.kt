package liqp.ext.filters.numbers

import liqp.nodes.RenderContext
import java.math.BigDecimal
import java.math.RoundingMode

class RoundFilter() :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    var ret = value

    if (value is Number && params.size == 1) {
      val places = (params[0] as Long).toInt()

      if (value is Int) {
        ret = String.format(context.locale, "%." + places + "f", value.toDouble())
      } else {
        val rounded = BigDecimal(value.toDouble()).setScale(places, RoundingMode.HALF_UP).toDouble()

        ret = String.format(context.locale, "%." + places + "f", rounded)
      }
    }

    return ret
  }
}
