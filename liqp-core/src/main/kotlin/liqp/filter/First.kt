package liqp.filter

import liqp.context.LContext

class First : LFilter() {

  /**
   * first(array)
   *
   * Get the first element of the passed in array
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.asIterable(value).firstOrNull()
  }
}
