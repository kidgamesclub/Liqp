package liqp.filter

import liqp.context.LContext
import java.util.regex.Pattern

class Split : LFilter() {

  /**
   * split(input, delimiter = ' ')
   *
   * Split a string on a matching pattern
   *
   * E.g. {{ "a~b" | split:'~' | first }} #=> 'a'
   */
  override fun onFilterAction(params: FilterParams, value: Any?,
                              context: LContext): Any? {

    val original = context.asString(value)
    val delimiter = context.asString(params[0]) ?: ""

    return original
        ?.split(("(?<!^)" + Pattern.quote(delimiter)).toRegex())
        ?.dropLastWhile { it.isEmpty() }
  }
}
