package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class EscapeOnceFilter : LFilter() {

  /**
   * escape_once(input)
   *
   * returns an escaped version of html without affecting
   * existing escaped entities
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val str = context.asString(value)
    return str?.replace("&(?!([a-zA-Z]+|#[0-9]+|#x[0-9A-Fa-f]+);)".toRegex(), "&amp;")
        ?.replace("<", "&lt;")
        ?.replace(">", "&gt;")
        ?.replace("\"", "&quot;")
  }
}
