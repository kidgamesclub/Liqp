package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class HFilter : LFilter() {

  /**
   * h(input)
   *
   * Alias for: escape
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val escape:EscapeFilter by context.parseSettings.filters
    return escape.onFilterAction(context, value, params)
  }
}

