package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import java.util.regex.Matcher
import java.util.regex.Pattern

class ReplaceFirst : LFilter() {

  /*
     * replace_first(input, string, replacement = '')
     *
     * Replace the first occurrences of a string with another
     */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {

    val original = context.asString(value)
    val needle:String = context.asString(params[0]) ?: return original
    val replacement = context.asString(params[1]) ?: ""

    return original?.replaceFirst(Pattern.quote(needle).toRegex(), Matcher.quoteReplacement(replacement))
  }
}
