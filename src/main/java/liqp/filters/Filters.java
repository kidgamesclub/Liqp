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
          new Url_Encode()));
  }

  private final Map<String, Filter> filters;

  @Builder
  private Filters(@NonNull @Singular List<Filter> filters) {
    this.filters = StreamEx.of(filters)
          .mapToEntry(t -> t.name, t -> t)
          .toImmutableMap();
  }

  public Filters withFilters(Filter... filters) {
    return new Filters(ImmutableList.<Filter>builder()
          .addAll(this.filters.values())
          .add(filters)
          .build());
  }

  public FiltersBuilder toBuilder () {
    return new FiltersBuilder().filters(this.filters.values());
  }


  public Filter getFilter(String filterName) {
    final Filter b = filters.get(filterName);
    if (b == null) {
      throw new IllegalArgumentException("No filter exists with name: " + filterName);
    }
    return b;
  }
}
