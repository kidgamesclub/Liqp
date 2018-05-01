package liqp.filter

import liqp.context.LContext

class Last : LFilter() {

  /**
   * last(array)
   *
   * Get the last element of the passed in array
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asIterable(value).lastOrNull()
  }
}
