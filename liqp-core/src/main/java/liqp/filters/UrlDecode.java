package liqp.filters;

import java.net.URLDecoder;
import liqp.nodes.RenderContext;

public class UrlDecode extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        try {
            return URLDecoder.decode(super.asString(value), "UTF-8");
        }
        catch (Exception e) {
            return value;
        }
    }
}
