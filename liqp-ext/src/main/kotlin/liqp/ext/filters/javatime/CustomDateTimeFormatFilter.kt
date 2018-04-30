package liqp.ext.filters.javatime

import com.google.common.base.Preconditions
import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

class CustomDateTimeFormatFilter : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    var ret = value

    if (value is TemporalAccessor && params.size == 1) {
      val formatter = DateTimeFormatter.ofPattern(params[0], context.locale)

      ret = formatter.format(value)
    }

    return ret
  }
}
