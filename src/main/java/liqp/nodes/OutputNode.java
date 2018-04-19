package liqp.nodes;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class OutputNode implements LNode {

    private LNode expression;
    private List<FilterNode> filters;

    public OutputNode(LNode expression) {
        this.expression = expression;
        this.filters = new ArrayList<FilterNode>();
    }

    public void addFilter(FilterNode filter) {
        filters.add(filter);
    }

    @Override
    public Object render(RenderContext context) {

        Object value = expression.render(context);

        for (FilterNode node : filters) {
            value = node.apply(value, context);
        }

        return value;
    }
}
