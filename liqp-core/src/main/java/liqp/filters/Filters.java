package liqp.filters;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import one.util.streamex.StreamEx;

@Value
public class Filters {
  private static final Filters DEFAULT = createDefaultFilters();

  public static Filters getDefaultFilters() {
    return DEFAULT;
  }

  private static Filters createDefaultFilters() {
    // Register all standard filters.
    return new Filters(ImmutableList.of(
          new Abs(),
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
          new UrlEncode()));
  }

  private final Map<String, LFilter> filters;

  @Builder
  private Filters(@NonNull @Singular List<LFilter> filters) {
    this.filters = StreamEx.of(filters)
          .mapToEntry(LFilter::getName, t -> t)
          .toImmutableMap();
  }

  public Filters withFilters(LFilter... filters) {
    return new Filters(ImmutableList.<LFilter>builder()
          .addAll(this.filters.values())
          .add(filters)
          .build());
  }

  public FiltersBuilder toBuilder() {
    return new FiltersBuilder().filters(this.filters.values());
  }

  public LFilter getFilter(String filterName) {
    final LFilter b = filters.get(filterName);
    if (b == null) {
      throw new IllegalArgumentException("No filter exists with name: " + filterName);
    }
    return b;
  }
}
