package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.OffsetDateTime

class MinusWeeksFilter : DateAdjustmentFilter() {
  override val offsetAdjust = OffsetDateTime::minusWeeks
  override val localDateTimeAdjust = LocalDateTime::minusWeeks
}
