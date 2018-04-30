package liqp.filter

import liqp.context.LContext
import liqp.isFalsy

class Default : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return when {
      value.isFalsy()-> params[0]
      else-> value
    }
  }
}
