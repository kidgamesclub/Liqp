package liqp.filters;

import liqp.nodes.RenderContext;

class Escape extends Filter {

    /*
     * escape(input)
     *
     * escape a string
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        String str = super.asString(value);

        return str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
