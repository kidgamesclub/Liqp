package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class Downcase : LFilter() {

  /**
   * downcase(input)
   *
   * convert a input string to DOWNCASE
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asString(value)?.toLowerCase()
  }
}
