package liqp.ext.filters.strings

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.params.FilterParams
import liqp.filter.LFilter

class ToIntegerFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asLong(value) ?: throw LiquidRenderingException("Expected a double value")
  }
}
