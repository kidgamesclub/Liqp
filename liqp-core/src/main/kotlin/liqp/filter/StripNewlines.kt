package liqp.filter

import liqp.context.LContext

class StripNewlines : LFilter() {

  /*
     * strip_newlines(input) click to toggle source
     *
     * Remove all newlines from the string
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    return super.asString(value).replace("[\r\n]++".toRegex(), "")
  }
}
