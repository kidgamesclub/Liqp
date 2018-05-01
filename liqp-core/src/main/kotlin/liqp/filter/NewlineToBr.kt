package liqp.filter

import liqp.context.LContext

class NewlineToBr : LFilter() {

  /**
   * newline_to_br(input)
   *
   * Add <br /> tags in front of all newlines in input string
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {

    return context.asString(value)
        ?.replace("[\n]".toRegex(), "<br />\n")
  }
}
