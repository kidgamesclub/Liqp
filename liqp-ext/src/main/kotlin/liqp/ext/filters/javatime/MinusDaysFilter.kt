package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.time.LocalDate
import java.time.ZonedDateTime

class MinusDaysFilter : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
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
