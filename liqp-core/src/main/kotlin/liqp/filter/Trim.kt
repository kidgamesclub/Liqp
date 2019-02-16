package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class Trim : LFilter() {

  /**
   * parenthesis (input, string)
   *
   * add one string to another
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val content = context.asString(value) ?: ""
    return content.trim()
  }
}
