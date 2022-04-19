package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.params.FilterParams
import liqp.filter.LFilter
import java.time.LocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

typealias OffsetAdjustment = OffsetDateTime.(Long)->OffsetDateTime
typealias LocalTimeAdjustment = LocalTime.(Long)->LocalTime
typealias LocalDateTimeAdjustment = LocalDateTime.(Long)->LocalDateTime

abstract class DateAdjustmentFilter : LFilter() {

  abstract val offsetAdjust:OffsetAdjustment
  abstract val localTimeAdjust:LocalTimeAdjustment
  abstract val localDateTimeAdjust:LocalDateTimeAdjustment

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val num = params[0, 0L]
    return when (value) {
      null-> null
      is OffsetDateTime -> value.offsetAdjust(num)
      is LocalTime -> value.localTimeAdjust(num)
      is LocalDate -> value.toLocalDateTime().localDateTimeAdjust(num)
      is LocalDateTime -> value.localDateTimeAdjust(num)
//      is DateTimeTz -> value.toOffsetDateTime().offsetAdjust(num)
      is Date-> value.toZonedDateTime().offsetAdjust(num)
      else -> value
    }
  }
}
