package liqp.ext.filters.javatime
import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import liqp.nodes.RenderContext


import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

class PlusMonthsFilter : DateAdjustmentFilter() {
  override val offsetAdjust: OffsetAdjustment = OffsetDateTime::plusMonths
  override val localDateTimeAdjust = LocalDateTime::plusMonths
}
