package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class ReverseFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asIterable(value).reversed()
  }
}
