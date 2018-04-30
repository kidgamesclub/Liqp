package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

class PlusMinutesFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusMinutes
  override val localDateTimeAdjust = LocalDateTime::plusMinutes
  override val localTimeAdjust = LocalTime::plusMinutes
}
