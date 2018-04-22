package liqp.ext.filters.javatime

import liqp.filters.Filter
import liqp.nodes.RenderContext
import java.time.LocalTime
import java.time.ZonedDateTime

class PlusMinutesFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    val num = if (params.size == 1) params[0] as Long else 0

    return when (value) {
      is ZonedDateTime-> value.plusMinutes(num)
      is LocalTime-> value.plusMinutes(num)
      else-> value
    }
  }
}
