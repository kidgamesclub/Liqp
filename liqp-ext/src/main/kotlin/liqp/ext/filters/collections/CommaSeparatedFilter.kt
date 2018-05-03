package liqp.ext.filters.collections

import liqp.context.LContext
import liqp.params.FilterParams
import liqp.filter.LFilter

class CommaSeparatedFilter : LFilter() {
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return when(value) {
      is Iterable<*>-> value.joinToString(separator = ", ")
      else-> value
    }
  }
}
