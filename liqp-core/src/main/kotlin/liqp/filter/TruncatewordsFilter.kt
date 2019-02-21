package liqp.filter

import com.google.common.base.Splitter
import liqp.context.LContext
import liqp.params.FilterParams
import liqp.safeSlice

val splitter = Splitter.onPattern("\\s++").omitEmptyStrings()

class TruncatewordsFilter : LFilter() {


  /**
   * truncatewords(input, words = 15, truncate_string = "...")
   *
   * Truncate a string down to x words
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      val text = asString(value) ?: return null
      val words = splitter.splitToList(text)
      //)text.split("\\s++".toPattern())
      val wordCount:Int = params[0, 15]
      val truncateString = params[1] ?: "..."

      return when {
        wordCount >= words.size -> text
        else -> words.safeSlice(0, wordCount).joinToString(separator = " ") + truncateString
      }
    }
  }
}
