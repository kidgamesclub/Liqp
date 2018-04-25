package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class IsoDateTimeFormatFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
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
