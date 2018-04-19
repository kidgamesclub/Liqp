package liqp.tags;

import liqp.nodes.FilterNode;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

public class Assign extends Tag {

    /*
     * Assigns some value to a variable
     */
    @Override
    public Object render(RenderContext context, LNode... nodes) {

        String id = String.valueOf(nodes[0].render(context));

        FilterNode filter = null;
        LNode expression;

        if(nodes.length >= 3) {
            filter = (FilterNode)nodes[1];
            expression = nodes[2];
        }
        else {
            expression = nodes[1];
        }

        Object value = expression.render(context);

        if(filter != null) {
            value = filter.apply(value, context);
        }

        // Assign causes variable to be saved "globally"
        context.setRoot(id, value);

        return "";
    }
}
