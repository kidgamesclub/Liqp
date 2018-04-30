package liqp.filter

import liqp.context.LContext

class Last : LFilter() {

  /**
   * last(array)
   *
   * Get the last element of the passed in array
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.asIterable(value).lastOrNull()
  }
}
