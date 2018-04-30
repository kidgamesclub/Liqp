package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.OffsetDateTime

class PlusDaysFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusDays
  override val localDateTimeAdjust = LocalDateTime::plusDays
}
