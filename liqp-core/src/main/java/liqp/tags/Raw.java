package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;
import liqp.context.LContext;

public class Raw extends LTag {

    /*
     * temporarily disable tag processing to avoid syntax conflicts.
     */
    @Override
    public Object render(LContext context, LNode... nodes) {
        return nodes[0].render(context);
    }
}
