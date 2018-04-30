package liqp.filter

import liqp.context.LContext

class Truncate : LFilter() {

  /**
     * truncate(input, length = 50, truncate_string = "...")
     *
     * Truncate a string down to x characters
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      val v = value ?: return null;
      val text = context.asString(v)!!

      val length = context.asLong(params[0])?.toInt() ?: 50
      val truncateString = context.asString(params[1]) ?: "..."

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
}
