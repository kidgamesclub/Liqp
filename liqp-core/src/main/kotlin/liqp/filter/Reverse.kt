package liqp.filter

import liqp.context.LContext

class Reverse : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return context.asIterable(value).reversed()
  }
}
