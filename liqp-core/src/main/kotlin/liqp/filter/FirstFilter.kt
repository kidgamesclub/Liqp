package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class FirstFilter : LFilter() {

  /**
   * first(array)
   *
   * Get the first element of the passed in array
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asIterable(value).firstOrNull()
  }
}
