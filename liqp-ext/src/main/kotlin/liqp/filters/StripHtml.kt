package liqp.filters

import liqp.LValue
import liqp.nodes.RenderContext
import org.jsoup.Jsoup

internal class StripHtml : Filter() {

  /*
     * strip_html(input)
     *
     * Remove all HTML tags from the string
     */
  public override fun apply(context: RenderContext, value: Any?, vararg params: Any): Any {

    val html = LValue.asString(value)

    return Jsoup.parse(html).text()
  }
}
