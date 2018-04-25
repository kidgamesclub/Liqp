package liqp.filter

import liqp.context.LContext

class Times : LFilter() {

  /*
     * times(input, operand)
     *
     * multiplication
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    var value = value

    if (value == null) {
      value = 0L
    }

    super.checkParams(params, 1)

    val rhsObj = params[0]

    return if (super.isInteger(value) && super.isInteger(rhsObj)) {
      super.asNumber(value).toLong() * super.asNumber(rhsObj).toLong()
    } else super.asNumber(value).toDouble() * super.asNumber(rhsObj).toDouble()
  }
}
