package liqp.filter

import liqp.context.LContext

class H : LFilter() {

  /*
     * h(input)
     *
     * Alias for: escape
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    val escape = getFilter("escape") as Escape
    return escape.apply(context, value, params)
  }
}
