package liqp.nodes;

import com.google.common.collect.ImmutableList;
import java.util.List;
import liqp.node.LNode;
import liqp.filter.FilterChain;
import liqp.filter.FilterInstance;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import one.util.streamex.StreamEx;

@Getter
public class OutputNode implements LNode {

  private LNode expression;
  private List<FilterNode> filters;

  @Builder
  OutputNode(LNode expression, @Singular List<FilterNode> filters) {
    this.expression = expression;
    this.filters = ImmutableList.copyOf(filters);
  }

  @Override
  public Object render(LContext context) {

    if (filters.isEmpty()) {
      return expression.render(context);
    } else {
      final List<FilterInstance> filters = StreamEx.of(this.filters)
            .map(FilterNode::getInstance)
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

  public static class OutputNodeBuilder {
    public OutputNodeBuilder value(Object value) {
      return expression(new AtomNode(value));
    }
  }
}
