package liqp.filter

import java.util.regex.Matcher
import java.util.regex.Pattern
import liqp.context.LContext

class ReplaceFirst : LFilter() {

  /*
     * replace_first(input, string, replacement = '')
     *
     * Replace the first occurrences of a string with another
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

    return original.replaceFirst(Pattern.quote(needle.toString()).toRegex(), Matcher.quoteReplacement(replacement))
  }
}
