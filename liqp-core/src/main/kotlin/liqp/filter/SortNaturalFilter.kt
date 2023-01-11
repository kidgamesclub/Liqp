package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import java.util.*

class SortNaturalFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      if (!isIterable(value)) {
        return value
      }
      return asIterable(value)
          .sortedBy { context.asString(it)?.lowercase(Locale.getDefault()) }
    }
  }
}
