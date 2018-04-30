package liqp.filter

import liqp.context.LContext

class Downcase : LFilter() {

  /**
   * downcase(input)
   *
   * convert a input string to DOWNCASE
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.asString(value)?.toLowerCase()
  }
}
