package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import kotlin.math.absoluteValue

class AbsFilter : LFilter() {
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? = context {
    asLong(value)?.absoluteValue ?: asDouble(value)?.absoluteValue ?: 0
  }
}
