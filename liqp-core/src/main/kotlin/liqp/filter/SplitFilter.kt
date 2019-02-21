package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import java.util.regex.Pattern

class SplitFilter : LFilter() {

  /**
   * split(input, delimiter = ' ')
   *
   * Split a string on a matching pattern
   *
   * E.g. {{ "a~b" | split:'~' | first }} #=> 'a'
   */
  override fun onFilterAction(context: LContext, value: Any?,
                              params: FilterParams): Any? {

    val original = context.asString(value)
    val delimiter = context.asString(params[0]) ?: ""

    return original
        ?.split(("(?<!^)" + Pattern.quote(delimiter)).toRegex())
        ?.dropLastWhile { it.isEmpty() }
  }
}
