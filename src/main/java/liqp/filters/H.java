package liqp.filters;

class H extends Filter {

    /*
     * h(input)
     *
     * Alias for: escape
     */
    @Override
    public Object apply(Object value, Object... params) {
      final Escape escape = (Escape) getFilter("escape");
      return escape.apply(value, params);
    }
}
