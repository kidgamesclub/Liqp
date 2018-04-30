package liqp.ext.filters.colors

import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.awt.Color

class ToRgbFilter : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return try {
      val decoded = when (value) {
        is Color -> value
        else -> Color.decode(value.toString())
      }
      "${decoded.red},${decoded.green},${decoded.blue}"
    } catch (e: Exception) {
      value
    }
  }
}
