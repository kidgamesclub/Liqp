package liqp.filters;

import liqp.nodes.RenderContext;

class Capitalize extends Filter {

    /*
     * (Object) capitalize(input)
     *
     * capitalize words in the input sentence
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        String original = super.asString(value);

        if (original.isEmpty()) {
            return original;
        }

        char first = original.charAt(0);

        return Character.toUpperCase(first) + original.substring(1);
    }
}
