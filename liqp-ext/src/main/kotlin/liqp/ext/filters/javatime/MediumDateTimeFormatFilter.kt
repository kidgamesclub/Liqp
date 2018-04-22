package liqp.ext.filters.javatime
import liqp.nodes.RenderContext


import java.time.format.FormatStyle
import java.util.Locale

class MediumDateTimeFormatFilter(locale: Locale) : DateTimeFormatFilter("medium", FormatStyle.MEDIUM, locale)
