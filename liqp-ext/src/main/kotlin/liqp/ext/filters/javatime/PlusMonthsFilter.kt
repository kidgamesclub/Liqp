package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.OffsetDateTime

class PlusMonthsFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusMonths
  override val localDateTimeAdjust = LocalDateTime::plusMonths
}
