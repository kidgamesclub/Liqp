package liqp.ext.filters

import liqp.ext.filters.collections.CommaSeparatedFilter
import liqp.ext.filters.collections.HtmlBulletsFilter
import liqp.ext.filters.colors.DarkenFilter
import liqp.ext.filters.colors.ToRgbFilter
import liqp.ext.filters.javatime.CustomDateTimeFormatFilter
import liqp.ext.filters.javatime.FullDateTimeFormatFilter
import liqp.ext.filters.javatime.IsoDateTimeFormatFilter
import liqp.ext.filters.javatime.LongDateTimeFormatFilter
import liqp.ext.filters.javatime.MediumDateTimeFormatFilter
import liqp.ext.filters.javatime.MinusDaysFilter
import liqp.ext.filters.javatime.MinusHoursFilter
import liqp.ext.filters.javatime.MinusMinutesFilter
import liqp.ext.filters.javatime.MinusMonthsFilter
import liqp.ext.filters.javatime.MinusSecondsFilter
import liqp.ext.filters.javatime.MinusWeeksFilter
import liqp.ext.filters.javatime.MinusYearsFilter
import liqp.ext.filters.javatime.PlusDaysFilter
import liqp.ext.filters.javatime.PlusHoursFilter
import liqp.ext.filters.javatime.PlusMinutesFilter
import liqp.ext.filters.javatime.PlusMonthsFilter
import liqp.ext.filters.javatime.PlusSecondsFilter
import liqp.ext.filters.javatime.PlusWeeksFilter
import liqp.ext.filters.javatime.PlusYearsFilter
import liqp.ext.filters.javatime.ShortDateTimeFormatFilter
import liqp.ext.filters.strings.StripHtmlFilter
import liqp.ext.filters.strings.ToDoubleFilter
import liqp.ext.filters.strings.ToIntegerFilter
import liqp.ext.filters.strings.USPhoneNumberFormatFilter
import liqp.filter.Filters

val EXTRA_FILTERS = Filters(
    CommaSeparatedFilter(),
    HtmlBulletsFilter(),
    DarkenFilter(),
    ToRgbFilter(),
    CustomDateTimeFormatFilter(),
    FullDateTimeFormatFilter(),
    IsoDateTimeFormatFilter(),
    LongDateTimeFormatFilter(),
    MediumDateTimeFormatFilter(),
    MinusDaysFilter(),
    MinusHoursFilter(),
    MinusMinutesFilter(),
    MinusMonthsFilter(),
    MinusSecondsFilter(),
    MinusWeeksFilter(),
    MinusYearsFilter(),
    PlusDaysFilter(),
    PlusHoursFilter(),
    PlusMinutesFilter(),
    PlusMonthsFilter(),
    PlusSecondsFilter(),
    PlusWeeksFilter(),
    PlusYearsFilter(),
    ShortDateTimeFormatFilter(),
    StripHtmlFilter(),
    ToDoubleFilter(),
    ToIntegerFilter(),
    USPhoneNumberFormatFilter()

)

class ExtraFilters {
  companion object {
    @JvmStatic
    val extraFilters = EXTRA_FILTERS
  }
}
