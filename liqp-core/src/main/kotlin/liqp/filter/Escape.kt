package liqp.filter

import liqp.context.LContext

class Escape : LFilter() {

  /**
   * escape(input)
   *
   * escape a string
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    val str = context.asString(value)
    return str?.replace("&", "&amp;")
        ?.replace("<", "&lt;")
        ?.replace(">", "&gt;")
        ?.replace("\"", "&quot;")
  }
}
