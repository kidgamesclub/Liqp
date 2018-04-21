package liqp.filters;

import liqp.nodes.RenderContext;

class Upcase extends Filter {

    /*
     * upcase(input)
     *
     * convert a input string to UPCASE
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        return super.asString(value).toUpperCase();
    }
}
