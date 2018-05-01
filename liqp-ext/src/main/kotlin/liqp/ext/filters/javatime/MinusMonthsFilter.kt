package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.time.LocalDate
import java.time.ZonedDateTime

class MinusMonthsFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val num = params[0, 0L]

    val ret: Any

    return when (value) {
      is ZonedDateTime -> value.minusMonths(num)
      is LocalDate -> value.minusMonths(num)
      else -> value
    }
  }
}
