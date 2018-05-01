package liqp.filter

import liqp.context.LContext

class Strip : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return when(value) {
      is String-> value.trim()
      else-> value
    }
  }
}
