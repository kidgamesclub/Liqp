package liqp.filter

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.params.FilterParams
import liqp.swallow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.util.*

class Date : LFilter() {

  /**
   * (Object) date(input, format)
   *
   * Format a date
   *
   * %a - The abbreviated weekday name (``Sun'')
   * %A - The  full  weekday  name (``Sunday'')
   * %b - The abbreviated month name (``Jan'')
   * %B - The  full  month  name (``January'')
   * %c - The preferred local date and time representation
   * %d - Day of the month (01..31)
   * %H - Hour of the day, 24-hour clock (00..23)
   * %I - Hour of the day, 12-hour clock (01..12)
   * %j - Day of the year (001..366)
   * %m - Month of the year (01..12)
   * %M - Minute of the hour (00..59)
   * %p - Meridian indicator (``AM''  or  ``PM'')
   * %S - Second of the minute (00..60)
   * %U - Week  number  of the current year,
   *      starting with the first Sunday as the first
   *      day of the first week (00..53)
   * %W - Week  number  of the current year,
   *      starting with the first Monday as the first
   *      day of the first week (00..53)
   * %w - Day of the week (Sunday is 0, 0..6)
   * %x - Preferred representation for the date alone, no time
   * %X - Preferred representation for the time alone, no date
   * %y - Year without a century (00..99)
   * %Y - Year with century
   * %Z - Time zone name
   * %% - Literal ``%'' character
   */
  override fun onFilterAction(context: LContext, value: Any?,
                              params: FilterParams): Any? {

    context.run {
      val zone = context.zoneId
      val locale = context.locale
      val date: TemporalAccessor? = when (value) {
        null -> return value //Exits completely
        "now" -> ZonedDateTime.now(zone)
        "now()" -> ZonedDateTime.now(zone)
        is java.util.Date -> ZonedDateTime.ofInstant(value.toInstant(), zone)
        is OffsetDateTime -> value.atZoneSameInstant(zone)
        is Number -> ZonedDateTime.ofInstant(Instant.ofEpochSecond(value.toLong()), zone)
        else -> tryParse(context, value.toString(), locale, zone)
      } ?: throw LiquidRenderingException("Unable to extract date from $value")

      // Default date format provided by render context.  Defaults to 'c' (see format table below)
      val outputFormat = asString(params[0])
      if (outputFormat?.isNotBlank() != true) {
        //Default format
        val formatter = findNamedFormat(locale, context.renderSettings.defaultDateFormat)!!
        //Ensure we have a zonedDateTime because the default format accesses HOUR, etc
        return formatter.format(date)
      }

      val formatBuilder = DateTimeFormatterBuilder()

      val chars = outputFormat.iterator()
      while (chars.hasNext()) {
        val ch = chars.nextChar()
        if (ch == '%' && chars.hasNext()) {
          val formatKey = chars.nextChar()
          val namedFormat = findNamedFormat(locale, formatKey)
          when (namedFormat) {
            null -> formatBuilder.appendLiteral("%").appendLiteral(formatKey)
            else -> formatBuilder.append(namedFormat)
          }
        } else {
          formatBuilder.appendLiteral(ch)
        }
      }

      return formatBuilder.toFormatter(locale).format(date)
    }
  }

  /**
   * Try to parse `str` into a Date and return this Date as seconds
   * since EPOCH, or null if it could not be parsed.
   */
  private fun tryParse(context:LContext, str: String, locale: Locale, zone: ZoneId): ZonedDateTime? {
    if (context.isIntegral(str)) {
      return ZonedDateTime.ofInstant(Instant.ofEpochSecond(context.asLong(str)!!), zone)
    }

    val parsed = parsers
        .getOrPut(locale, {
          setOf(
              DateTimeFormatter.ISO_ZONED_DATE_TIME,
              DateTimeFormatter.ISO_OFFSET_DATE_TIME,
              DateTimeFormatter.ISO_DATE_TIME,
              *parseFmtStrings.map {
                DateTimeFormatter.ofPattern(it, locale)
                    .withResolverStyle(ResolverStyle.SMART)

              }.toTypedArray())
        })
        .mapNotNull {
          swallow {
            val parsed = it.parse(str)
            parsed
          }
        }
        .firstOrNull() ?: return null

    // Do some safe checks to see what type of date/time we can support
    return when {
      parsed.isSupported(ChronoField.OFFSET_SECONDS) -> OffsetDateTime.from(parsed).atZoneSameInstant(zone)
      parsed.isSupported(ChronoField.HOUR_OF_DAY) -> LocalDateTime.from(parsed).atZone(zone)
      else -> LocalDate.from(parsed).atStartOfDay(zone)
    }
  }
}

internal val parseFmtStrings = listOf(
    "yyyy-MM-dd hh:mm:ss a",
    "yyyy/MM/dd hh:mm:ss a",
    "yyyy-MM-dd hh:mm a",
    "yyyy/MM/dd hh:mm a",
    "yyyy-MM-dd HH:mm:ss",
    "yyyy/MM/dd HH:mm:ss",
    "yyyy-MM-dd HH:mm",
    "yyyy/MM/dd HH:mm",
    "yyyy-MM-dd",
    "yyyy/MM/dd",
    "EEE MMM dd HH:mm:ss yyyy")

private val formatTable = mutableMapOf<Locale, kotlin.collections.Map<Char, DateTimeFormatter>>()
private val parsers = mutableMapOf<Locale, Set<DateTimeFormatter>>()

fun findNamedFormat(locale: Locale, ch: Char): DateTimeFormatter? {
  return formatTable.getOrPut(locale, {
    mapOf(
        '%' to DateTimeFormatter.ofPattern("%", locale),
        // %a - The abbreviated weekday name (``Sun'')
        'a' to DateTimeFormatter.ofPattern("EEE", locale),
        // %A - The  full  weekday  name (``Sunday'')
        'A' to DateTimeFormatter.ofPattern("EEEE", locale),
        // %b - The abbreviated month name (``Jan'')
        'b' to DateTimeFormatter.ofPattern("MMM", locale),
        'h' to DateTimeFormatter.ofPattern("MMM", locale),
        // %B - The  full  month  name (``January'')
        'B' to DateTimeFormatter.ofPattern("MMMM", locale),
        // %c - The preferred local date and time representation
        'c' to DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", locale),
        // %d - Day of the month (01..31)
        'd' to DateTimeFormatter.ofPattern("dd", locale),
        // %H - Hour of the day, 24-hour clock (00..23)
        'H' to DateTimeFormatter.ofPattern("HH", locale),
        // %I - Hour of the day, 12-hour clock (01..12)
        'I' to DateTimeFormatter.ofPattern("hh", locale),
        // %j - Day of the year (001..366)
        'j' to DateTimeFormatter.ofPattern("DDD", locale),
        // %m - Month of the year (01..12)
        'm' to DateTimeFormatter.ofPattern("MM", locale),
        // %M - Minute of the hour (00..59)
        'M' to DateTimeFormatter.ofPattern("mm", locale),
        // %p - Meridian indicator (``AM''  or  ``PM'')
        'p' to DateTimeFormatter.ofPattern("a", locale),
        // %S - Second of the minute (00..60)
        'S' to DateTimeFormatter.ofPattern("ss", locale),
        // %U - Week  number  of the current year,
        //      starting with the first Sunday as the first
        //      day of the first week (00..53)
        'U' to DateTimeFormatter.ofPattern("ww", locale),
        // %W - Week  number  of the current year,
        //      starting with the first Monday as the first
        //      day of the first week (00..53)
        'W' to DateTimeFormatter.ofPattern("ww", locale),
        // %w - Day of the week (Sunday is 0, 0..6)
        'w' to DateTimeFormatter.ofPattern("e", locale),
        // %x - Preferred representation for the date alone, no time
        'x' to DateTimeFormatter.ofPattern("MM/dd/yy", locale),
        // %X - Preferred representation for the time alone, no date
        'X' to DateTimeFormatter.ofPattern("HH:mm:ss", locale),
        // %y - Year without a century (00..99)
        'y' to DateTimeFormatter.ofPattern("yy", locale),
        // %Y - Year with century
        'Y' to DateTimeFormatter.ofPattern("yyyy", locale),
        // %Z - Time zone name
        'Z' to DateTimeFormatter.ofPattern("z", locale))
  })[ch]
}
