package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.LocalTime
import java.time.ZonedDateTime

class MinusHoursFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    val num = if (params.size == 1) params[0] as Long else 0

    val ret: Any

    if (value is ZonedDateTime) {
      ret = value.minusHours(num)
    } else if (value is LocalTime) {
      ret = value.minusHours(num)
    } else {
      ret = value
    }

    return ret
  }
}
