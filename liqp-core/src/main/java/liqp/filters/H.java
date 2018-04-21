package liqp.filters;

import liqp.nodes.RenderContext;

class H extends Filter {

    /*
     * h(input)
     *
     * Alias for: escape
     */
    @Override
    public Object apply(RenderContext context, Object value, Object... params) {
      final Escape escape = (Escape) getFilter("escape");
      return escape.apply(context, value, params);
    }
}
