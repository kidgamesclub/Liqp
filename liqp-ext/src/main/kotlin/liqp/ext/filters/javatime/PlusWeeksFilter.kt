package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.OffsetDateTime

class PlusWeeksFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusWeeks
  override val localDateTimeAdjust = LocalDateTime::plusWeeks
}
