package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.params.FilterParams
import liqp.filter.LFilter
import java.time.LocalTime
import java.time.ZonedDateTime

class MinusMinutesFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val num = params[0, 0L]

    return when (value) {
      is ZonedDateTime -> value.minusMinutes(num)
      is LocalTime -> value.minusMinutes(num)
      else -> value
    }
  }
}
