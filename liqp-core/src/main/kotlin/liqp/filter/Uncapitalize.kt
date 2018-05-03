package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * {{ 'McBob' | uncapitalize }} -> mcBob
 */
class Uncapitalize : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asString(value)?.decapitalize() ?: value
  }
}
