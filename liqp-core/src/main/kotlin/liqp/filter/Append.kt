package liqp.filter

import liqp.context.LContext

/**
 * {{ foo.bar | append:'john' }}
 *
 * add one string to another
 */
class Append : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      return asString(value) + asString(params[0])
    }
  }
}
