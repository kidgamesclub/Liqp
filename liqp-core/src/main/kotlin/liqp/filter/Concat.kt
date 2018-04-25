package liqp.filter

import liqp.context.LContext

/**
 * Combines two lists.  If the parameters and/or value are not lists, they will be converted before
 * combining.  Null values are stripped
 */
class Concat : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    context.run {
      return asIterable(value).toList() + asIterable(params[0]).toList()
    }
  }
}
