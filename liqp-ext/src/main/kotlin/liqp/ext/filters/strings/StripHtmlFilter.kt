package liqp.ext.filters.strings

import liqp.ControlResult.NO_CONTENT
import liqp.context.LContext
import liqp.filter.FilterParams
import liqp.filter.LFilter
import org.jsoup.Jsoup

internal class StripHtmlFilter : LFilter() {

  /**
   * strip_html(input)
   *
   * Remove all HTML tags from the string
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any {
    val html = context.asString(value) ?: return NO_CONTENT
    return Jsoup.parse(html).text()
  }
}
