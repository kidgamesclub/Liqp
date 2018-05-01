package liqp.filter

import liqp.context.LContext

class Replace : LFilter() {

  /**
   * replace(input, string, replacement = '')
   *
   * Replace occurrences of a string with another
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val original = context.asString(value)
    val needle:String = context.asString(params[0]) ?: return original
    return original?.replace(needle, context.asString(params[1]) ?: "")
  }
}
