package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;
import liqp.context.LContext;

public class Capture extends LTag {

    /*
     * Block tag that captures text into a variable
     */
    @Override
    public Object render(LContext context, LNode... nodes) {

        String id = super.asString(nodes[0].render(context));

        LNode block = nodes[1];

        // Capture causes variable to be saved "globally"
        context.set(id, block.render(context));

        return null;
    }
}
