package liqp.filter

import liqp.context.LContext

class Lstrip : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return when (value) {
      is String -> value.trimStart()
      else -> value
    }
  }
}
