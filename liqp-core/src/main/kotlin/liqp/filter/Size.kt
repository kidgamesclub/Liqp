package liqp.filter

import liqp.context.LContext

class Size : LFilter() {

  /**
   * size(input)
   *
   * Return the size of an array or of an string
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.size(value)
  }
}
