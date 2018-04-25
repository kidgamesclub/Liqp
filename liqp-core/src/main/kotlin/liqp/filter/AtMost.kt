package liqp.filter

import liqp.context.LContext
/**
 * {{ 10 | at_most:32 }} -> 10
 * {{ 45 | at_most:32 }} -> 32
 */
class AtMost : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      return when {
        params.size == 0 -> value
        else -> {
          val result = asDouble(value) ?: 0.0
          val param = asDouble(params[0]) ?: 0.0
          Math.min(result, param)
        }
      }
    }
  }
}
