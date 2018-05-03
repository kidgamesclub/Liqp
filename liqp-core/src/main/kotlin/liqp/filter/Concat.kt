package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * Combines two lists.  If the parameters and/or value are not lists, they will be converted before
 * combining.  Null values are stripped
 */
class Concat : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      return asIterable(value).toList() + asIterable(params[0]).toList()
    }
  }
}
