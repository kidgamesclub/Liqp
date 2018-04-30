package liqp.filter

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator
import liqp.context.LContext

class SortNatural : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    context.run {
      if (!isIterable(value)) {
        return value
      }
      val iterable = asIterable(value).toMutableList()
      iterable.sortBy { toString().toLowerCase() }
      return iterable.toList()
    }
  }
}
