package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.format.FormatStyle
import java.util.Locale

class FullDateTimeFormatFilter(locale: Locale) : DateTimeFormatFilter("full", FormatStyle.FULL, locale)
