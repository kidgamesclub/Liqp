package liqp.filter

import liqp.context.LContext

class EscapeOnce : LFilter() {

  /*
     * escape_once(input)
     *
     * returns an escaped version of html without affecting
     * existing escaped entities
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    val str = super.asString(value)

    return str.replace("&(?!([a-zA-Z]+|#[0-9]+|#x[0-9A-Fa-f]+);)".toRegex(), "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
  }
}
