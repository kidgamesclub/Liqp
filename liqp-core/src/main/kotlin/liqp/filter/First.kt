package liqp.filter

import liqp.context.LContext

class First : LFilter() {

  /**
   * first(array)
   *
   * Get the first element of the passed in array
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asIterable(value).firstOrNull()
  }
}
