package liqp.filter

import liqp.context.LContext

class Strip : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return when(value) {
      is String-> value.trim()
      else-> value
    }
  }
}
