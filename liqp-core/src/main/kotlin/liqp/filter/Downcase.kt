package liqp.filter

import liqp.context.LContext

class Downcase : LFilter() {

  /*
     * downcase(input)
     *
     * convert a input string to DOWNCASE
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    return super.asString(value).toLowerCase()
  }
}
