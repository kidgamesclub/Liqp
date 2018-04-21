package liqp.filters;

import liqp.nodes.RenderContext;

public class Rstrip extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if (!super.isString(value)) {
            return value;
        }

        return super.asString(value).replaceAll("\\s+$", "");
    }
}
