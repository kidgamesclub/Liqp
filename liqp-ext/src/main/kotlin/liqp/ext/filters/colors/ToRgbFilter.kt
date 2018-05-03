package liqp.ext.filters.colors

import liqp.context.LContext
import liqp.params.FilterParams
import liqp.filter.LFilter
import java.awt.Color

class ToRgbFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
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
