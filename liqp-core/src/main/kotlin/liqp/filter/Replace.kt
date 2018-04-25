package liqp.filter

import liqp.context.LContext

class Replace : LFilter() {

  /*
     * replace(input, string, replacement = '')
     *
     * Replace occurrences of a string with another
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    val original = super.asString(value)

    val needle = super.get(0, params)
    var replacement = ""

    if (needle == null) {
      throw RuntimeException("invalid pattern: " + needle!!)
    }

    if (params.size >= 2) {

      val obj = super.get(1, params) ?: throw RuntimeException("invalid replacement: " + needle!!)

      replacement = super.asString(super.get(1, params))
    }

    return original.replace(needle.toString(), replacement)
  }
}
