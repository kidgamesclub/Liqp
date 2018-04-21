package liqp.filters;

import java.net.URLEncoder;
import liqp.nodes.RenderContext;

public class Url_Encode extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        try {
            return URLEncoder.encode(super.asString(value), "UTF-8");
        }
        catch (Exception e) {
            return value;
        }
    }
}
