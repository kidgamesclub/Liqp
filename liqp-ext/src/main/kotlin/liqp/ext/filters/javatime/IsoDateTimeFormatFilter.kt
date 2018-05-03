package liqp.ext.filters.javatime
import liqp.context.LContext
import liqp.params.FilterParams
import liqp.filter.LFilter

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class IsoDateTimeFormatFilter : LFilter() {

override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    var ret = value

    if (value is LocalDate) {
      ret = DateTimeFormatter.ISO_DATE.format(value)
    } else if (value is LocalTime) {
      ret = DateTimeFormatter.ISO_TIME.format(value)
    } else if (value is TemporalAccessor) {
      ret = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
    }

    return ret
  }
}
