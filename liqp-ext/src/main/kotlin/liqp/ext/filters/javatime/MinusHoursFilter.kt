package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.time.LocalTime
import java.time.ZonedDateTime

class MinusHoursFilter : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    val num = params[0, 0L]

    return when (value) {
      is ZonedDateTime -> value.minusHours(num)
      is LocalTime -> value.minusHours(num)
      else -> value
    }
  }
}
