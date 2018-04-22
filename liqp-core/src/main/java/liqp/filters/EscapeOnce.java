package liqp.filters;

import liqp.nodes.RenderContext;

class EscapeOnce extends Filter {

    /*
     * escape_once(input)
     *
     * returns an escaped version of html without affecting
     * existing escaped entities
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        String str = super.asString(value);

        return str.replaceAll("&(?!([a-zA-Z]+|#[0-9]+|#x[0-9A-Fa-f]+);)", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
