package liqp.filter

import liqp.context.LContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.Map

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
  override fun onFilterAction(params: FilterParams, value: Any?,
                              chain: FilterChainPointer,
                              context: LContext): Any? {

    context.run {
      val zone = context.zoneId
      val locale = context.locale
      val offsetDateTime: OffsetDateTime? = when (value) {
        null -> return value //Exits completely
        "now" -> OffsetDateTime.now()
        is java.util.Date -> OffsetDateTime.ofInstant(value.toInstant(), zone)
        is OffsetDateTime -> value
        is Number -> OffsetDateTime.ofInstant(Instant.ofEpochSecond(value.toLong()), zone)
        else -> tryParse(value.toString(), locale, zone)
      }

      try {

        val outputFormat = asString(params[0])
        if (outputFormat == null || outputFormat.isBlank()) {
          return value
        }

        val builder = StringBuilder()

        val chars = outputFormat.iterator()
        while (chars.hasNext()) {
          var ch = chars.nextChar()

          if (ch == '%') {
            if (chars.hasNext()) {
              ch = chars.nextChar()
            }
            if (!chars.hasNext()) {
              // a trailing (single) '%' sign: just append it
              builder.append("%")
              return value
            }

            val simpleDateFormat = getFormat(locale, ch)

            if (simpleDateFormat == null) {
              // no valid date-format: append the '%' and the 'next'-char
              builder.append("%").append(ch)
            } else {
              builder.append(simpleDateFormat.format(offsetDateTime))
            }
          } else {
            builder.append(ch)
          }
        }

        return builder.toString()
      } catch (e: Exception) {
        return value
      }
    }
  }

  /**
   * Try to parse `str` into a Date and return this Date as seconds
   * since EPOCH, or null if it could not be parsed.
   */
  private fun tryParse(str: String, locale: Locale, zone: ZoneId): OffsetDateTime? {
    parseFormats
        .mapNotNull {
          try {
            SimpleDateFormat(it, locale).parse(str)
          } catch (e: Exception) {
            return null
          }
        }
        .map { OffsetDateTime.ofInstant(it.toInstant(), zone) }
        .firstOrNull()
    // Could not parse the string into a meaningful date, return null.
    return null
  }

  companion object {

    init {

    }
  }
}

internal val parseFormats = setOf("yyyy-MM-dd HH:mm:ss", "EEE MMM dd hh:mm:ss yyyy")
internal val formats = mutableMapOf<Locale, Map<Char, SimpleDateFormat>>()

fun getFormat(locale: Locale, ch: Char): SimpleDateFormat? {
  return formats.getOrPut(locale, {
    mapOf(
        '%' to SimpleDateFormat("%", locale),
        // %a - The abbreviated weekday name (``Sun'')
        'a' to SimpleDateFormat("EEE", locale),
        // %A - The  full  weekday  name (``Sunday'')
        'A' to SimpleDateFormat("EEEE", locale),
        // %b - The abbreviated month name (``Jan'')
        'b' to SimpleDateFormat("MMM", locale),
        'h' to SimpleDateFormat("MMM", locale),
        // %B - The  full  month  name (``January'')
        'B' to SimpleDateFormat("MMMM", locale),
        // %c - The preferred local date and time representation
        'c' to SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", locale),
        // %d - Day of the month (01..31)
        'd' to SimpleDateFormat("dd", locale),
        // %H - Hour of the day, 24-hour clock (00..23)
        'H' to SimpleDateFormat("HH", locale),
        // %I - Hour of the day, 12-hour clock (01..12)
        'I' to SimpleDateFormat("hh", locale),
        // %j - Day of the year (001..366)
        'j' to SimpleDateFormat("DDD", locale),
        // %m - Month of the year (01..12)
        'm' to SimpleDateFormat("MM", locale),
        // %M - Minute of the hour (00..59)
        'M' to SimpleDateFormat("mm", locale),
        // %p - Meridian indicator (``AM''  or  ``PM'')
        'p' to SimpleDateFormat("a", locale),
        // %S - Second of the minute (00..60)
        'S' to SimpleDateFormat("ss", locale),
        // %U - Week  number  of the current year,
        //      starting with the first Sunday as the first
        //      day of the first week (00..53)
        'U' to SimpleDateFormat("ww", locale),
        // %W - Week  number  of the current year,
        //      starting with the first Monday as the first
        //      day of the first week (00..53)
        'W' to SimpleDateFormat("ww", locale),
        // %w - Day of the week (Sunday is 0, 0..6)
        'w' to SimpleDateFormat("F", locale),
        // %x - Preferred representation for the date alone, no time
        'x' to SimpleDateFormat("MM/dd/yy", locale),
        // %X - Preferred representation for the time alone, no date
        'X' to SimpleDateFormat("HH:mm:ss", locale),
        // %y - Year without a century (00..99)
        'y' to SimpleDateFormat("yy", locale),
        // %Y - Year with century
        'Y' to SimpleDateFormat("yyyy", locale),
        // %Z - Time zone name
        'Z' to SimpleDateFormat("z", locale))
  })[ch]
}
