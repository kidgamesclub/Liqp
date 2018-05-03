package liqp.filter

import liqp.context.LContext
import liqp.isFalsy
import liqp.params.FilterParams

class Default : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return when {
      value.isFalsy()-> params[0]
      else-> value
    }
  }
}
