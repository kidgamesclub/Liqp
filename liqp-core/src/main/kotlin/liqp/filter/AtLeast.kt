package liqp.filter

import liqp.context.LContext

/**
 * {{ 10 | at_least:32 }} -> 32
 * {{ 45 | at_least:32 }} -> 45
 */
class AtLeast : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      return when {
        params.size == 0 -> value
        else -> {
          val result = asDouble(value) ?: 0.0
          val param = asDouble(params[0]) ?: 0.0
          Math.max(result, param)
        }
      }
    }
  }
}
