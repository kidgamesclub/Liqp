package liqp.filter

import liqp.context.LContext

class Prepend : LFilter() {

  /**
   * (Object) append(input, string)
   *
   * add one string to another
   */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      return asString(params[0]) + asString(value)
    }
  }
}
