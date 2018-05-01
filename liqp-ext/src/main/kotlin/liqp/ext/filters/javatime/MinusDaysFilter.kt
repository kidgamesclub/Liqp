package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.time.LocalDate
import java.time.ZonedDateTime

class MinusDaysFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val num = params[0, 0L]

    return if (value is ZonedDateTime) {
      value.minusDays(num)
    } else if (value is LocalDate) {
      value.minusDays(num)
    } else {
      value
    }
  }
}
