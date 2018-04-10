package liqp.tags;

import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

public class Continue extends Tag {

    @Override
    public Object render(RenderContext context, LNode... nodes) {
        return CONTINUE;
    }
}
