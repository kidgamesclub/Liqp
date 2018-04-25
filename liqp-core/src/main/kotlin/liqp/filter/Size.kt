package liqp.filter

import liqp.context.LContext

class Size : LFilter() {

  /*
     * size(input)
     *
     * Return the size of an array or of an string
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (super.isArray(value)) {
      return context.asIterable(value)!!.size
    }

    if (super.isString(value)) {
      return super.asString(value).length
    }

    return if (super.isNumber(value)) {
      // we're only using 64 bit longs, no BigIntegers or the like.
      // So just return 8 (the number of bytes in a long).
      8
    } else 0

    // boolean or nil
  }
}
