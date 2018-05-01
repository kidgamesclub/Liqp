package liqp.filter

import liqp.context.LContext

class Reverse : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asIterable(value).reversed()
  }
}
