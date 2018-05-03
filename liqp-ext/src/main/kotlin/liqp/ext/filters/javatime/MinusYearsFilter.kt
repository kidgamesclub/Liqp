package liqp.ext.filters.javatime

import java.time.LocalDateTime
import java.time.OffsetDateTime

class MinusYearsFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::minusYears
  override val localDateTimeAdjust = LocalDateTime::minusYears

}
