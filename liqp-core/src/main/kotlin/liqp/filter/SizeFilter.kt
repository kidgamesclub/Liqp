package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class SizeFilter : LFilter() {

  /**
   * size(input)
   *
   * Return the size of an array or of an string
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.size(value)
  }
}
