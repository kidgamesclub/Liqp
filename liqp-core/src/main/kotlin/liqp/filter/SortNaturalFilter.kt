package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class SortNaturalFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      if (!isIterable(value)) {
        return value
      }
      return asIterable(value)
          .sortedBy{context.asString(it)?.toLowerCase()}
    }
  }
}
