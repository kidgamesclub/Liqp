package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class Num : LFilter() {

  /**
   * Applies the filter to the value, and returns the current rendered value.  If the return value should not
   * set the current rendered value context, then [noActionInstance], or [PostFilter.NO_ACTION] should be
   * returned instead.
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asNumber(value)

  }
}
