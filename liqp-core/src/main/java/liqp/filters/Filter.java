package liqp.filters;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import liqp.LValue;
import liqp.nodes.RenderContext;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;

/**
 * Output markup takes filters. Filters are simple methods. The first parameter is always the output of the left side of
 * the filter. The return value of the filter will be the new left value when the next filter is run. When there are no
 * more filters, the template will receive the resulting string.
 * <p/>
 * -- https://github.com/Shopify/liquid/wiki/Liquid-for-Designers
 */
public abstract class Filter extends LValue implements LFilter {
  public static final Converter<String, String> SNAKE_CONVERTER = UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);

  /**
   * A map holding all filters.
   */
  private static final Map<String, LFilter> DEFAULT_FILTERS = createDefaultFilters();

  /**
   * Retrieves a filter with a specific name.
   *
   * @param name the name of the filter to retrieve.
   *
   * @return a filter with a specific name.
   */
  public static Filter getFilter(String name) {

    LFilter filter = DEFAULT_FILTERS.get(name);

    if (filter == null) {
      throw new RuntimeException("unknown filter: " + name);
    }

    return (Filter) filter;
  }

  /**
   * Returns all default filters.
   *
   * @return all default filters.
   */
  public static Map<String, LFilter> getDefaultFilters() {
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
    final String truncated = this.getClass().getSimpleName().replaceAll("Filter$", "");
    this.name = SNAKE_CONVERTER.convert(truncated);
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
  protected abstract Object apply(RenderContext context, Object value, Object... params);

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

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  final void checkParams(Object[] params, int min, int max) {

    if (params == null || params.length < min || params.length > max) {
      throw new RuntimeException("Liquid error: wrong number of arguments (given " +
            (params == null ? 1 : (params.length + 1)) + " expected " + (min + 1) + ".." + (max + 1) + ")");
    }
  }

  @Override
  public void doFilter(@NotNull FilterParams params,
                       @NotNull FilterChainPointer chain,
                       @NotNull RenderContext context,
                       @NotNull AtomicReference<Object> result) {
    final Object value = chain.continueChain();
    final Object filterResult = this.apply(context, value, params.resolve(context));
    result.set(filterResult);
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

  private static Map<String, LFilter> createDefaultFilters() {
    return StreamEx.of(
          // Initialize all standard filters.
          (LFilter) new Abs(),
          new Append(),
          new AtLeast(),
          new AtMost(),
          new Capitalize(),
          new Ceil(),
          new Compact(),
          new Concat(),
          new Date(),
          new Default(),
          new DividedBy(),
          new Downcase(),
          new Escape(),
          new EscapeOnce(),
          new First(),
          new Floor(),
          new H(),
          new Join(),
          new Last(),
          new Lstrip(),
          new liqp.filters.Map(),
          new Minus(),
          new Modulo(),
          new NewlineToBr(),
          new Plus(),
          new Prepend(),
          new Remove(),
          new RemoveFirst(),
          new Replace(),
          new ReplaceFirst(),
          new Reverse(),
          new Round(),
          new Rstrip(),
          new Size(),
          new Slice(),
          new Sort(),
          new SortNatural(),
          new Split(),
          new Strip(),
          new StripNewlines(),
          new Times(),
          new Truncate(),
          new Truncatewords(),
          new Uniq(),
          new Upcase(),
          new UrlDecode(),
          new UrlEncode())
          .mapToEntry(LFilter::getName, f -> f)
          .toImmutableMap();
  }
}
