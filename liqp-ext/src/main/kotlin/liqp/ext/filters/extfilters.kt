package liqp.ext.filters

import liqp.ext.filters.collections.CommaSeparatedFilter
import liqp.ext.filters.collections.HtmlBulletsFilter
import liqp.ext.filters.colors.DarkenFilter
import liqp.ext.filters.colors.ToRgbFilter
import liqp.ext.filters.javatime.*
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
    InTimeZoneFilter(),
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
