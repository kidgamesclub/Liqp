package liqp.filter

import liqp.context.LContext

class Prepend : LFilter() {

  /*
     * (Object) append(input, string)
     *
     * add one string to another
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    return super.asString(super.get(0, params)) + super.asString(value)
  }
}
