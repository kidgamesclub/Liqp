package liqp.filters;

import liqp.nodes.RenderContext;

class NewlineToBr extends Filter {

    /*
     * newline_to_br(input)
     *
     * Add <br /> tags in front of all newlines in input string
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        return super.asString(value).replaceAll("[\n]", "<br />\n");
    }
}
