package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class RemoveFilter : LFilter() {

  /**
   * remove(input, string)
   *
   * remove a substring
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val original = context.asString(value)
    val needle:Any = params[0] ?: throw RuntimeException("invalid pattern: " + params[0])

    return original?.replace(needle.toString(), "")
  }
}
