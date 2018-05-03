package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * {{ 10 | at_least:32 }} -> 32
 * {{ 45 | at_least:32 }} -> 45
 */
class AtLeast : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      return when {
        params.size == 0 -> value
        else -> {
          context.max(value, params[0])
        }
      }
    }
  }
}
