package liqp.filter

import liqp.context.LContext

class Truncate : LFilter() {

  /**
     * truncate(input, length = 50, truncate_string = "...")
     *
     * Truncate a string down to x characters
     */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      val v = value ?: return null;
      val text = context.asString(v)!!

      val length = params[0] ?: 50
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
