package liqp.filters;

import liqp.nodes.RenderContext;

class Strip_Newlines extends Filter {

    /*
     * strip_newlines(input) click to toggle source
     *
     * Remove all newlines from the string
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        return super.asString(value).replaceAll("[\r\n]++", "");
    }
}
