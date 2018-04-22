package liqp.filters;

import java.util.regex.Pattern;
import liqp.nodes.RenderContext;

class RemoveFirst extends Filter {

    /*
     * remove_first(input, string)
     *
     * remove the first occurrences of a substring
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {

        String original = super.asString(value);

        Object needle = super.get(0, params);

        if (needle == null) {
            throw new RuntimeException("invalid pattern: " + needle);
        }

        return original.replaceFirst(Pattern.quote(String.valueOf(needle)), "");
    }
}
