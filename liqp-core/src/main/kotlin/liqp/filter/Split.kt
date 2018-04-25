package liqp.filter

import java.util.regex.Pattern
import liqp.context.LContext

class Split : LFilter() {

  /*
     * split(input, delimiter = ' ')
     *
     * Split a string on a matching pattern
     *
     * E.g. {{ "a~b" | split:'~' | first }} #=> 'a'
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    val original = super.asString(value)

    val delimiter = super.asString(super.get(0, params))

    return original.split(("(?<!^)" + Pattern.quote(delimiter)).toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  }
}
