package liqp.nodes;

import com.google.common.collect.ImmutableList;
import java.util.List;
import liqp.node.LNode;
import liqp.filter.FilterInstance;
import liqp.filter.FilterParams;
import liqp.filter.LFilter;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;

@Getter
public class FilterNode implements LNode {

  private final LFilter filter;
  private final List<LNode> params;

  @Builder
  public FilterNode(@NotNull LFilter filter, @Singular List<LNode> params) {
    this.filter = filter;
    this.params = ImmutableList.copyOf(params);
  }

  /**
   * Creates an instance of this filter for rendering.  This wrapper class allows for delayed resolution of
   * filter params, in case this filter is never invoked.
   */
  public FilterInstance getInstance() {
    return new FilterInstance(filter, new FilterParams(params));
  }

  @Override
  public List<LNode> children() {
    return ImmutableList.<LNode>builder()
          .addAll(params)
          .build();
  }

  @Override
  public Object render(LContext context) {
    throw new IllegalStateException("cannot render a filter");
  }
}
