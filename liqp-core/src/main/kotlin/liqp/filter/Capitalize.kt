package liqp.filter

import liqp.context.LContext

/**
 * {{ 'bob jones' | capitalize }} -> Bob jones
 */
class Capitalize : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      return when {
        value == null-> null
        size(value) == 0 -> value
        else-> asString(value).decapitalize()
      }
    }
  }
}
