package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import com.google.common.base.Preconditions
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.Locale
import liqp.filters.Filter


abstract class DateTimeFormatFilter(name: String, private val style: FormatStyle, private val locale: Locale) : Filter(name) {

  init {
    Preconditions.checkNotNull(style, "style must not be null")
    Preconditions.checkNotNull(locale, "locale must not be null")
  }

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    var ret = value

    if (value is LocalDate) {
      val formatter = DateTimeFormatter.ofLocalizedDate(style).withLocale(locale)

      ret = formatter.format(value)
    } else if (value is LocalTime) {
      val styleToUse = if (style == FormatStyle.SHORT) style else FormatStyle.MEDIUM
      val formatter = DateTimeFormatter.ofLocalizedTime(styleToUse).withLocale(locale)

      ret = formatter.format(value)
    } else if (value is TemporalAccessor) {
      val formatter = DateTimeFormatter.ofLocalizedDateTime(style).withLocale(locale)

      ret = formatter.format(value)
    } else if (value is Date) {
      //I don't think this QUITE works... maybe need some help on it.
      val zone = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(value.timezoneOffset / 60))
      val offsetDate = OffsetDateTime.ofInstant(value.toInstant(), zone)
      val formatter = DateTimeFormatter.ofLocalizedDate(style).withLocale(locale)
      ret = formatter.format(offsetDate)
    }

    return ret
  }
}
