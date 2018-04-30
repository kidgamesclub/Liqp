package liqp.filter

import liqp.context.LContext

class StripNewlines : LFilter() {

  /**
   * strip_newlines(input) click to toggle source
   *
   * Remove all newlines from the string
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.asString(value)?.replace("[\r\n]++".toRegex(), "")
  }
}
