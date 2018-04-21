package liqp.filters;

import liqp.nodes.RenderContext;

public class Abs extends Filter {

    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        if (super.isInteger(value) || super.canBeInteger(value)) {
            return Math.abs(super.asNumber(value).longValue());
        }

        if (super.isNumber(value) || super.canBeDouble(value)) {
            return Math.abs(super.asNumber(value).doubleValue());
        }

        return value;
    }
}
