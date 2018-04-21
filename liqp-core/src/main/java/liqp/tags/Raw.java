package liqp.tags;

import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

public class Raw extends Tag {

    /*
     * temporarily disable tag processing to avoid syntax conflicts.
     */
    @Override
    public Object render(RenderContext context, LNode... nodes) {
        return nodes[0].render(context);
    }
}
