package liqp.filter

import java.util.LinkedHashSet
import liqp.context.LContext
import liqp.params.FilterParams

class Uniq : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return LinkedHashSet(context.asIterable(value).toList()).toList()
  }
}
