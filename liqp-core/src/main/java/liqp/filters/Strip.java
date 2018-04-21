package liqp.filters;

import liqp.nodes.RenderContext;

public class Strip extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if (!super.isString(value)) {
            return value;
        }

        return super.asString(value).trim();
    }
}
