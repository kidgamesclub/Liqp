package liqp.filters;

import liqp.nodes.RenderContext;

public class Ceil extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if (!super.isNumber(value)) {
            return value;
        }

        return (long)Math.ceil(super.asNumber(value).doubleValue());
    }
}
