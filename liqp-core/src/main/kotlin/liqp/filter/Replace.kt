package liqp.filter

import liqp.context.LContext

class Replace : LFilter() {

  /**
   * replace(input, string, replacement = '')
   *
   * Replace occurrences of a string with another
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    val original = context.asString(value)
    val needle:String = context.asString(params[0]) ?: throw RuntimeException("invalid pattern: " + params[0])
    return original?.replace(needle, context.asString(params[1]) ?: "")
  }
}
