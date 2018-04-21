package liqp.tags;

import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

public class Capture extends Tag {

    /*
     * Block tag that captures text into a variable
     */
    @Override
    public Object render(RenderContext context, LNode... nodes) {

        String id = super.asString(nodes[0].render(context));

        LNode block = nodes[1];

        // Capture causes variable to be saved "globally"
        context.set(id, block.render(context));

        return null;
    }
}
