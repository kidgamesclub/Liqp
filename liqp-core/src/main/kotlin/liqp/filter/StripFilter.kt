package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class StripFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return when(value) {
      is String-> value.trim()
      else-> value
    }
  }
}
