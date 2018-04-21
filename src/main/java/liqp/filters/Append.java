package liqp.filters;

import liqp.nodes.RenderContext;

class Append extends Filter {

    /*
     * (Object) append(input, string)
     *
     * add one string to another
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        return super.asString(value) + super.asString(super.get(0, params));
    }
}
