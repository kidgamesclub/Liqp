package liqp.filter

import liqp.context.LContext

class H : LFilter() {

  /**
   * h(input)
   *
   * Alias for: escape
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    val escape:Escape by context.filters
    return escape.onFilterAction(params, value, chain, context)
  }
}

