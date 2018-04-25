package liqp.filter

import liqp.context.LContext

class Truncate : LFilter() {

  /*
     * truncate(input, length = 50, truncate_string = "...")
     *
     * Truncate a string down to x characters
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (value == null) {
      return ""
    }

    val text = super.asString(value)
    var length = 50
    var truncateString = "..."

    if (params.size >= 1) {
      length = super.asNumber(super.get(0, params)).toInt()
    }

    if (params.size >= 2) {
      truncateString = super.asString(super.get(1, params))
    }

    if (truncateString.length >= length) {
      return truncateString
    }

    if (length == text.length) {
      return text
    }

    if (length >= text.length + truncateString.length) {
      return text
    }

    val remainingChars = length - truncateString.length

    return text.substring(0, remainingChars) + truncateString
  }
}
