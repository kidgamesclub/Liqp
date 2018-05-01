package liqp.filter

import liqp.context.LContext

class Rstrip : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return when(value) {
      is String-> value.trimEnd()
      else-> value
    }
  }
}
