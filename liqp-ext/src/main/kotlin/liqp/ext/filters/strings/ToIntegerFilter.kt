package liqp.ext.filters.strings

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter

class ToIntegerFilter : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.asLong(value) ?: throw LiquidRenderingException("Expected a double value")
  }
}
