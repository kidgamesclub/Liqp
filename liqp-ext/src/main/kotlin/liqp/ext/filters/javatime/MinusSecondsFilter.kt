package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

class MinusSecondsFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::minusSeconds
  override val localTimeAdjust: LocalTimeAdjustment = LocalTime::minusSeconds
  override val localDateTimeAdjust: LocalDateTimeAdjustment = LocalDateTime::minusSeconds
}
