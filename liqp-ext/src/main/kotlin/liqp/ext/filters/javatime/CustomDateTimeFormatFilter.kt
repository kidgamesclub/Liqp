package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import com.google.common.base.Preconditions
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Locale

class CustomDateTimeFormatFilter(private val locale: Locale) :Filter() {

  init {
    Preconditions.checkNotNull(locale, "locale must not be null")
  }

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    var ret = value

    if (value is TemporalAccessor && params.size == 1) {
      val formatter = DateTimeFormatter.ofPattern(params[0] as String, this.locale)

      ret = formatter.format(value)
    }

    return ret
  }
}
