package liqp.filters;

import java.util.Arrays;
import java.util.Map;
import liqp.LValue;
import liqp.nodes.RenderContext;
import one.util.streamex.StreamEx;

/**
 * Output markup takes filters. Filters are simple methods. The first parameter is always the output of the left side of
 * the filter. The return value of the filter will be the new left value when the next filter is run. When there are no
 * more filters, the template will receive the resulting string.
 * <p/>
 * -- https://github.com/Shopify/liquid/wiki/Liquid-for-Designers
 */
public abstract class Filter extends LValue {

  /**
   * A map holding all filters.
   */
  private static final Map<String, Filter> DEFAULT_FILTERS = createDefaultFilters();

  /**
   * Retrieves a filter with a specific name.
   *
   * @param name the name of the filter to retrieve.
   *
   * @return a filter with a specific name.
   */
  public static Filter getFilter(String name) {

    Filter filter = DEFAULT_FILTERS.get(name);

    if (filter == null) {
      throw new RuntimeException("unknown filter: " + name);
    }

    return filter;
  }

  /**
   * Returns all default filters.
   *
   * @return all default filters.
   */
  public static Map<String, Filter> getDefaultFilters() {
    return DEFAULT_FILTERS;
  }
  /**
   * The name of the filter.
   */
  public final String name;

  /**
   * Used for all package protected filters in the liqp.filters-package whose name is their class name lower cased.
   */
  protected Filter() {
    this.name = this.getClass().getSimpleName().toLowerCase();
  }

  /**
   * Creates a new instance of a Filter.
   *
   * @param name the name of the filter.
   */
  public Filter(String name) {
    this.name = name;
  }

  /**
   * Applies the filter on the 'value'.
   *
   * @param value  the string value `AAA` in: `{{ 'AAA' | f:1,2,3 }}`
   * @param params the values [1, 2, 3] in: `{{ 'AAA' | f:1,2,3 }}`
   *
   * @return the result of the filter.
   */
  public Object apply(Object value, Object... params) {

    // Default "no-op" filter.
    return value;
  }

  /**
   * Applies the filter on the 'value', with the given 'context'.
   *
   * @param value   the string value `AAA` in: `{{ 'AAA' | f:1,2,3 }}`
   * @param context the template context.
   * @param params  the values [1, 2, 3] in: `{{ 'AAA' | f:1,2,3 }}`
   *
   * @return the result of the filter.
   */
  public Object apply(Object value, RenderContext context, Object... params) {

    return apply(value, params);
  }

  /**
   * Check the number of parameters and throws an exception if needed.
   *
   * @param params   the parameters to check.
   * @param expected the expected number of parameters.
   */
  final void checkParams(Object[] params, int expected) {

    if (params == null || params.length != expected) {
      throw new RuntimeException("Liquid error: wrong number of arguments (given " +
            (params == null ? 1 : (params.length + 1)) + " for " + (expected + 1) + ")");
    }
  }

  final void checkParams(Object[] params, int min, int max) {

    if (params == null || params.length < min || params.length > max) {
      throw new RuntimeException("Liquid error: wrong number of arguments (given " +
            (params == null ? 1 : (params.length + 1)) + " expected " + (min + 1) + ".." + (max + 1) + ")");
    }
  }

  /**
   * Returns a value at a specific index from an array of parameters. If no such index exists, a RuntimeException is
   * thrown.
   *
   * @param index  the index of the value to be retrieved.
   * @param params the values.
   *
   * @return a value at a specific index from an array of parameters.
   */
  protected Object get(int index, Object... params) {

    if (index >= params.length) {
      throw new RuntimeException("error in filter '" + name +
            "': cannot get param index: " + index +
            " from: " + Arrays.toString(params));
    }

    return params[index];
  }

  private static Map<String, Filter> createDefaultFilters() {
    return StreamEx.of(
          // Initialize all standard filters.
          new Abs(),
          new Append(),
          new At_Least(),
          new At_Most(),
          new Capitalize(),
          new Ceil(),
          new Compact(),
          new Concat(),
          new Date(),
          new Default(),
          new Divided_By(),
          new Downcase(),
          new Escape(),
          new Escape_Once(),
          new First(),
          new Floor(),
          new H(),
          new Join(),
          new Last(),
          new Lstrip(),
          new liqp.filters.Map(),
          new Minus(),
          new Modulo(),
          new Newline_To_Br(),
          new Plus(),
          new Prepend(),
          new Remove(),
          new Remove_First(),
          new Replace(),
          new Replace_First(),
          new Reverse(),
          new Round(),
          new Rstrip(),
          new Size(),
          new Slice(),
          new Sort(),
          new Sort_Natural(),
          new Split(),
          new Strip(),
          new Strip_HTML(),
          new Strip_Newlines(),
          new Times(),
          new Truncate(),
          new Truncatewords(),
          new Uniq(),
          new Upcase(),
          new Url_Decode(),
          new Url_Encode())
          .mapToEntry(f -> f.name, f -> f)
          .toImmutableMap();
  }
}
