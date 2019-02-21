package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * {{ 10 | at_most:32 }} -> 10
 * {{ 45 | at_most:32 }} -> 32
 */
class AtMostFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      return when {
        params.size == 0 -> value
        else -> {
          context.min(value, params[0])
        }
      }
    }
  }
}
