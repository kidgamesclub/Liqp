package liqp.filter

import liqp.context.LContext

class Remove : LFilter() {

  /*
     * remove(input, string)
     *
     * remove a substring
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    val original = super.asString(value)

    val needle = super.get(0, params)

    if (needle == null) {
      throw RuntimeException("invalid pattern: " + needle!!)
    }

    return original.replace(needle.toString(), "")
  }
}
