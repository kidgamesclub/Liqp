package liqp.ext.filters.javatime

import liqp.context.LContext
import liqp.filter.LFilter
import liqp.join
import liqp.params.FilterParams
import java.util.*
import kotlin.time.Duration

class DurationFilter : LFilter("duration") {
    override fun onFilterAction(
        context: LContext, value: Any?, params: FilterParams): Any? {
        val durationString = context.asString(value) ?: return null
        val isoDuration = Duration.parseIsoString(durationString)
        return formatDuration(isoDuration)
    }

    private fun formatDuration(duration: Duration): String {
        val parts: MutableList<String> = ArrayList()
        return duration.toComponents { days, hours, minutes, seconds, _ ->
            if (days > 0) {
                parts.add(plural(days, "day"))
            }
            if (hours > 0) {
                parts.add(plural(hours, "hour"))
            }
            if (minutes > 0) {
                parts.add(plural(minutes, "minute"))
            }
            if(seconds > 0) {
                parts.add(plural(seconds, "second"))
            }
            parts.join(", ");
        }

    }
    private fun plural(num: Number, unit: String): String {
        return num.toString() + " " + unit + if (num.toLong() == 1L) "" else "s"
    }
}