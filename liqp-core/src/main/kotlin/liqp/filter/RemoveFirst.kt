package liqp.filter

import liqp.context.LContext
import java.util.regex.Pattern

class RemoveFirst : LFilter() {

  /**
   * remove_first(input, string)
   *
   * remove the first occurrences of a substring
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val original = context.asString(value)
    val needle:Any = params[0] ?: throw RuntimeException("invalid pattern: " + params[0])

    return original?.replaceFirst(Pattern.quote(needle.toString()).toRegex(), "")
  }
}
