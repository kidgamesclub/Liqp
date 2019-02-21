package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class StripNewlinesFilter : LFilter() {

  /**
   * strip_newlines(input) click to toggle source
   *
   * Remove all newlines from the string
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asString(value)?.replace("[\r\n]++".toRegex(), "")
  }
}
