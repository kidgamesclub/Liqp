package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.LocalDate
import java.time.ZonedDateTime
import liqp.filters.Filter

class PlusYearsFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    val num = if (params.size == 1) params[0] as Long else 0

    val ret: Any

    if (value is ZonedDateTime) {
      ret = value.plusYears(num)
    } else if (value is LocalDate) {
      ret = value.plusYears(num)
    } else {
      ret = value
    }

    return ret
  }
}
