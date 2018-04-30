package liqp.filter

import liqp.context.LContext

/**
 * {{ 'bob jones' | capitalize }} -> Bob jones
 */
class Capitalize : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.asString(value)?.capitalize() ?: value
  }
}
