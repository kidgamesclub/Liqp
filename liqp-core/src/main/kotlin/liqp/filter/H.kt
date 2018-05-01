package liqp.filter

import liqp.context.LContext

class H : LFilter() {

  /**
   * h(input)
   *
   * Alias for: escape
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val escape:Escape by context.filters
    return escape.onFilterAction(context, value, params)
  }
}

