package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.format.FormatStyle
import java.util.Locale

class LongDateTimeFormatFilter(locale: Locale) : DateTimeFormatFilter("long", FormatStyle.LONG, locale)
