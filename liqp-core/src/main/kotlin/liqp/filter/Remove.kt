package liqp.filter

import liqp.context.LContext

class Remove : LFilter() {

  /**
   * remove(input, string)
   *
   * remove a substring
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    val original = context.asString(value)
    val needle:Any = params[0] ?: throw RuntimeException("invalid pattern: " + params[0])

    return original?.replace(needle.toString(), "")
  }
}
