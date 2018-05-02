package liqp.filter

import liqp.context.LContext

class Truncatewords : LFilter() {

  /**
   * truncatewords(input, words = 15, truncate_string = "...")
   *
   * Truncate a string down to x words
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      val text = asString(value) ?: return null
      val words = text.split(' ').filter { it.isNotBlank() }
      val length = asInteger(params[0]) ?: return value
      val truncateString = params[1] ?: "..."

      return when {
        length >= words.size -> text
        else -> words.subList(0, length).joinToString(separator = " ") + truncateString
      }
    }
  }
}
