package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.OffsetDateTime

class PlusYearsFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusYears
  override val localDateTimeAdjust = LocalDateTime::plusYears
}
