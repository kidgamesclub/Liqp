package liqp.filters;

import liqp.nodes.RenderContext;

class Downcase extends Filter {

    /*
     * downcase(input)
     *
     * convert a input string to DOWNCASE
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        return super.asString(value).toLowerCase();
    }
}
