package liqp.filter

import liqp.context.LContext
import liqp.nodes.RenderContext

class Num : LFilter() {

  /**
   * Applies the filter to the value, and returns the current rendered value.  If the return value should not
   * set the current rendered value context, then [noActionInstance], or [PostFilter.NO_ACTION] should be
   * returned instead.
   */

  override fun onFilterAction(params: FilterParams, input: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return when(input) {
      null-> 0
      is Number-> input
      else-> input.toString().toDoubleOrNull() ?: 0
    }
  }
}
