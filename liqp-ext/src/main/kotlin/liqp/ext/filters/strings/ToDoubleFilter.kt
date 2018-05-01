package liqp.ext.filters.strings
import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.filter.FilterParams
import liqp.filter.LFilter

class ToDoubleFilter : LFilter() {

override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return context.asDouble(value) ?: throw LiquidRenderingException("Expected a double value")
  }
}
