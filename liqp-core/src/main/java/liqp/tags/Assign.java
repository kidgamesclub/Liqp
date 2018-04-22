package liqp.tags;

import java.util.List;
import liqp.filters.FilterChain;
import liqp.filters.FilterParams;
import liqp.filters.FilterInstance;
import liqp.nodes.FilterNode;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;
import one.util.streamex.StreamEx;

public class Assign extends Tag {

  /*
   * Assigns some value to a variable
   */
  @Override
  public Object render(RenderContext context, LNode... nodes) {
    final List<FilterInstance> filters = StreamEx.of(nodes)
          .filter(node -> node instanceof FilterNode)
          .map(node -> (FilterNode) node)
          .map(node -> new FilterInstance(node.getFilter(), new FilterParams(node.getParams())))
          .toImmutableList();
    final FilterChain chain = new FilterChain(context, filters, ref -> {
      LNode expression = nodes[1];
      ref.set(expression.render(context));
      return null;
    });

    final Object finalValue = chain.processFilters();
    String id = String.valueOf(nodes[0].render(context));
    // Assign causes variable to be saved "globally"
    context.setRoot(id, finalValue);
    return "";
  }
}
