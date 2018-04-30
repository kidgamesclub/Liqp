package liqp.filter

import liqp.context.LContext

class Truncatewords : LFilter() {

  /**
   * truncatewords(input, words = 15, truncate_string = "...")
   *
   * Truncate a string down to x words
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {

    val v = value ?: return null
    context.run {

      val text = asString(value) ?: return null
      val words = text.split(' ').filter { it.isNotBlank() }
      val length = asInteger(params[0]) ?: return value
      val truncateString = params[1] ?: "..."

      return {
        if (length >= words.size) text
        else words.joinToString(separator = " ",
            limit = length,
            truncated = truncateString)
      }
    }
  }
}
