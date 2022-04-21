package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.DateFilter
import liqp.filter.LFilter
import liqp.params.FilterParams
import kotlin.time.Duration

class PlusDurationFilter : LFilter("plus_duration") {
    override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
        val date = DateFilter.convertToZonedDateTime(context, value) ?: return null

        val duration = when (val duration = params.get<Any>(0)) {
            is Duration -> duration
            is java.time.Duration -> Duration.parseIsoString(duration.toString())
            else -> Duration.parseIsoString(duration.toString())
        }

        return date.plusSeconds(duration.inWholeSeconds)
    }

}