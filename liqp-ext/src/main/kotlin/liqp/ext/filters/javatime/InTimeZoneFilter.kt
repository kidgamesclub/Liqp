package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.filter.DateFilter
import liqp.filter.LFilter
import liqp.params.FilterParams
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class InTimeZoneFilter : LFilter("in_timezone", "in_time_zone") {
    override fun onFilterAction(
        context: LContext, value: Any?, params: FilterParams): Any? {
        val date = DateFilter.convertToZonedDateTime(context, value) ?: return null

        val zoneId = when (val timeZoneName = params.get<String>(0)) {
            in ZoneId.getAvailableZoneIds() -> ZoneId.of(timeZoneName)
            else -> TimeZone.getTimeZone(timeZoneName).toZoneId()
        }
        return date.withZoneSameInstant(zoneId)
    }
}