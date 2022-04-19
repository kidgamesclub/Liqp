package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

class MinusWeeksFilter : DateAdjustmentFilter() {
  override val offsetAdjust = OffsetDateTime::minusWeeks
  override val localTimeAdjust:LocalTimeAdjustment = {_->  this}
  override val localDateTimeAdjust = LocalDateTime::minusWeeks
}
