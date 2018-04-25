package liqp.filter

import java.text.SimpleDateFormat
import java.util.HashSet
import java.util.Locale
import liqp.context.LContext

class Date : LFilter() {

  /*
     * (Object) date(input, format)
     *
     * Reformat a date
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
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    try {
      val seconds: Long?

      if (super.asString(value) == "now") {
        seconds = System.currentTimeMillis() / 1000L
      } else if (super.isNumber(value)) {
        // No need to divide this by 1000, the param is expected to be in seconds already!
        seconds = super.asNumber(value!!).toLong()
      } else {
        seconds = trySeconds(super.asString(value)) // formatter.parse(super.asString(value)).getTime() / 1000L;

        if (seconds == null) {
          return value
        }
      }

      val date = java.util.Date(seconds * 1000L)
      val format = super.asString(super.get(0, params))

      if (format == null || format.trim { it <= ' ' }.isEmpty()) {
        return value
      }

      val calendar = java.util.Calendar.getInstance()
      calendar.time = date

      val builder = StringBuilder()

      var i = 0
      while (i < format.length) {

        val ch = format[i]

        if (ch == '%') {

          i++

          if (i == format.length) {
            // a trailing (single) '%' sign: just append it
            builder.append("%")
            break
          }

          val next = format[i]

          val javaFormat = LIQUID_TO_JAVA_FORMAT[next]

          if (javaFormat == null) {
            // no valid date-format: append the '%' and the 'next'-char
            builder.append("%").append(next)
          } else {
            builder.append(javaFormat.format(date))
          }
        } else {
          builder.append(ch)
        }
        i++
      }

      return builder.toString()
    } catch (e: Exception) {
      return value
    }
  }

  /*
     * Try to parse `str` into a Date and return this Date as seconds
     * since EPOCH, or null if it could not be parsed.
     */
  private fun trySeconds(str: String): Long? {

    for (pattern in datePatterns) {

      val parser = SimpleDateFormat(pattern, locale)

      try {
        val milliseconds = parser.parse(str).time
        return milliseconds / 1000L
      } catch (e: Exception) {
        // Just ignore and try the next pattern in `datePatterns`.
      }
    }

    // Could not parse the string into a meaningful date, return null.
    return null
  }

  companion object {

    private var locale = Locale.ENGLISH
    private val datePatterns = HashSet<String>()

    private val LIQUID_TO_JAVA_FORMAT = java.util.HashMap<Char, SimpleDateFormat>()

    init {
      addDatePattern("yyyy-MM-dd HH:mm:ss")
      addDatePattern("EEE MMM dd hh:mm:ss yyyy")
      init()
    }

    private fun init() {

      // %% - Literal ``%'' character
      LIQUID_TO_JAVA_FORMAT['%'] = SimpleDateFormat("%", locale)

      // %a - The abbreviated weekday name (``Sun'')
      LIQUID_TO_JAVA_FORMAT['a'] = SimpleDateFormat("EEE", locale)

      // %A - The  full  weekday  name (``Sunday'')
      LIQUID_TO_JAVA_FORMAT['A'] = SimpleDateFormat("EEEE", locale)

      // %b - The abbreviated month name (``Jan'')
      LIQUID_TO_JAVA_FORMAT['b'] = SimpleDateFormat("MMM", locale)
      LIQUID_TO_JAVA_FORMAT['h'] = SimpleDateFormat("MMM", locale)

      // %B - The  full  month  name (``January'')
      LIQUID_TO_JAVA_FORMAT['B'] = SimpleDateFormat("MMMM", locale)

      // %c - The preferred local date and time representation
      LIQUID_TO_JAVA_FORMAT['c'] = SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", locale)

      // %d - Day of the month (01..31)
      LIQUID_TO_JAVA_FORMAT['d'] = SimpleDateFormat("dd", locale)

      // %H - Hour of the day, 24-hour clock (00..23)
      LIQUID_TO_JAVA_FORMAT['H'] = SimpleDateFormat("HH", locale)

      // %I - Hour of the day, 12-hour clock (01..12)
      LIQUID_TO_JAVA_FORMAT['I'] = SimpleDateFormat("hh", locale)

      // %j - Day of the year (001..366)
      LIQUID_TO_JAVA_FORMAT['j'] = SimpleDateFormat("DDD", locale)

      // %m - Month of the year (01..12)
      LIQUID_TO_JAVA_FORMAT['m'] = SimpleDateFormat("MM", locale)

      // %M - Minute of the hour (00..59)
      LIQUID_TO_JAVA_FORMAT['M'] = SimpleDateFormat("mm", locale)

      // %p - Meridian indicator (``AM''  or  ``PM'')
      LIQUID_TO_JAVA_FORMAT['p'] = SimpleDateFormat("a", locale)

      // %S - Second of the minute (00..60)
      LIQUID_TO_JAVA_FORMAT['S'] = SimpleDateFormat("ss", locale)

      // %U - Week  number  of the current year,
      //      starting with the first Sunday as the first
      //      day of the first week (00..53)
      LIQUID_TO_JAVA_FORMAT['U'] = SimpleDateFormat("ww", locale)

      // %W - Week  number  of the current year,
      //      starting with the first Monday as the first
      //      day of the first week (00..53)
      LIQUID_TO_JAVA_FORMAT['W'] = SimpleDateFormat("ww", locale)

      // %w - Day of the week (Sunday is 0, 0..6)
      LIQUID_TO_JAVA_FORMAT['w'] = SimpleDateFormat("F", locale)

      // %x - Preferred representation for the date alone, no time
      LIQUID_TO_JAVA_FORMAT['x'] = SimpleDateFormat("MM/dd/yy", locale)

      // %X - Preferred representation for the time alone, no date
      LIQUID_TO_JAVA_FORMAT['X'] = SimpleDateFormat("HH:mm:ss", locale)

      // %y - Year without a century (00..99)
      LIQUID_TO_JAVA_FORMAT['y'] = SimpleDateFormat("yy", locale)

      // %Y - Year with century
      LIQUID_TO_JAVA_FORMAT['Y'] = SimpleDateFormat("yyyy", locale)

      // %Z - Time zone name
      LIQUID_TO_JAVA_FORMAT['Z'] = SimpleDateFormat("z", locale)
    }

    /**
     * Changes the locale.
     *
     * @param locale the new locale.
     */
    fun setLocale(locale: Locale) {
      Date.locale = locale
      init()
    }

    /**
     * Adds a new Date-pattern to be used when parsing a string to a Date.
     *
     * @param pattern the pattern.
     */
    fun addDatePattern(pattern: String?) {

      if (pattern == null) {
        throw NullPointerException("date-pattern cannot be null")
      }

      datePatterns.add(pattern)
    }

    /**
     * Removed a Date-pattern to be used when parsing a string to a Date.
     *
     * @param pattern the pattern.
     */
    fun removeDatePattern(pattern: String) {

      datePatterns.remove(pattern)
    }
  }
}
