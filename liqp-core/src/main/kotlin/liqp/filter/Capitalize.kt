package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * {{ 'bob jones' | capitalize }} -> Bob jones
 */
class Capitalize : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asString(value)?.capitalize() ?: value
  }
}
