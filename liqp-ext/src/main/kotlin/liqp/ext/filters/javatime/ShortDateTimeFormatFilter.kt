package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.format.FormatStyle
import java.util.Locale

class ShortDateTimeFormatFilter(locale: Locale) : DateTimeFormatFilter("short", FormatStyle.SHORT, locale)
