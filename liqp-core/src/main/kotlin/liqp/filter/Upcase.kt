package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class Upcase : LFilter("upcase", "upper") {

  /**
   * upcase(input)
   *
   * convert a input string to UPCASE
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asString(value)?.toUpperCase()
  }
}
