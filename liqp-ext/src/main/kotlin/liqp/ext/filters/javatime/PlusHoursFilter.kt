package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

class PlusHoursFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusHours
  override val localDateTimeAdjust = LocalDateTime::plusHours
  override val localTimeAdjust = LocalTime::plusHours
}
