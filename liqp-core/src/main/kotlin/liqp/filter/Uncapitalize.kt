package liqp.filter

import liqp.context.LContext

/**
 * {{ 'McBob' | uncapitalize }} -> mcBob
 */
class Uncapitalize : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.asString(value)?.decapitalize() ?: value
  }
}
