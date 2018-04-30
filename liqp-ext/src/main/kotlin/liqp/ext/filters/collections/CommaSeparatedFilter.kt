package liqp.ext.filters.collections

import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import liqp.nodes.RenderContext

class CommaSeparatedFilter : LFilter() {
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return when(value) {
      is Iterable<*>-> value.joinToString(separator = ", ")
      else-> value
    }
  }
}
