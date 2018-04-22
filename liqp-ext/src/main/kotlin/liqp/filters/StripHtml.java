package liqp.filters;

import liqp.nodes.RenderContext;
import org.jsoup.Jsoup;

class StripHtml extends Filter {

    /*
     * strip_html(input)
     *
     * Remove all HTML tags from the string
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        String html = super.asString(value);

        return Jsoup.parse(html).text();
    }
}
