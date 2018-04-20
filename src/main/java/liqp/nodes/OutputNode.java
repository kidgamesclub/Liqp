package liqp.nodes;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import liqp.filters.FilterChain;
import liqp.filters.FilterParams;
import liqp.filters.FilterWithParams;
import lombok.Getter;
import one.util.streamex.StreamEx;

@Getter
public class OutputNode implements LNode {

    private LNode expression;
    private List<FilterNode> filters;

    public OutputNode(LNode expression) {
        this.expression = expression;
        this.filters = new ArrayList<>();
    }

    public void addFilter(FilterNode filter) {
        filters.add(filter);
    }

    @Override
    public Object render(RenderContext context) {

      if (filters.isEmpty()) {
        return expression.render(context);
      } else {
        final List<FilterWithParams> filters = StreamEx.of(this.filters)
              .map(f -> new FilterWithParams(f.getFilter(), new FilterParams(f.getParams())))
              .toImmutableList();
        final FilterChain filterChain = new FilterChain(context, filters, ref -> {
          ref.set(expression.render(context));
          return ref.get();
        });
        return filterChain.processFilters();
      }
    }

  @Override
  public List<LNode> children() {
    return ImmutableList.<LNode>builder()
          .add(expression)
          .addAll(filters)
          .build();
  }
}
