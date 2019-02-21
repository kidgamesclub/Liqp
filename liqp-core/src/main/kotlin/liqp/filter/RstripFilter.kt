package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class RstripFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return when(value) {
      is String-> value.trimEnd()
      else-> value
    }
  }
}
