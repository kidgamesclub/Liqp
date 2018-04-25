package liqp.filter

import java.util.regex.Pattern
import liqp.context.LContext

class RemoveFirst : LFilter() {

  /*
     * remove_first(input, string)
     *
     * remove the first occurrences of a substring
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    val original = super.asString(value)

    val needle = super.get(0, params)

    if (needle == null) {
      throw RuntimeException("invalid pattern: " + needle!!)
    }

    return original.replaceFirst(Pattern.quote(needle.toString()).toRegex(), "")
  }
}
