package liqp.filter

import liqp.context.LContext

class Truncatewords : LFilter() {

  /*
     * truncatewords(input, words = 15, truncate_string = "...")
     *
     * Truncate a string down to x words
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (value == null) {
      return ""
    }

    val text = super.asString(value)
    val words = text.split("\\s++".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var length = 15
    var truncateString = "..."

    if (params.size >= 1) {
      length = super.asNumber(super.get(0, params)).toInt()
    }

    if (params.size >= 2) {
      truncateString = super.asString(super.get(1, params))
    }

    return if (length >= words.size) {
      text
    } else join(words, length) + truncateString
  }

  private fun join(words: Array<String>, length: Int): String {

    val builder = StringBuilder()

    for (i in 0 until length) {
      builder.append(words[i]).append(" ")
    }

    return builder.toString().trim { it <= ' ' }
  }
}
