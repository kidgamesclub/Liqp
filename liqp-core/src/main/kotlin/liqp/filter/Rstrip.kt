package liqp.filter

import liqp.context.LContext

class Rstrip : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return when(value) {
      is String-> value.trimEnd()
      else-> value
    }
  }
}
