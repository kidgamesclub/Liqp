package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class CustomDateTimeFormatFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    var ret = value

    if (value is TemporalAccessor && params.size == 1) {
      val formatter = DateTimeFormatter.ofPattern(params[0], context.locale)

      ret = formatter.format(value)
    }

    return ret
  }
}
