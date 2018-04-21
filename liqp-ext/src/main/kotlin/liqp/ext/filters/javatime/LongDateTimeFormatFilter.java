package liqp.ext.filters.javatime;

import java.time.format.FormatStyle;
import java.util.Locale;

public class LongDateTimeFormatFilter extends DateTimeFormatFilter {

    public LongDateTimeFormatFilter(Locale locale) {
        super("long", FormatStyle.LONG, locale);
    }

}
