package liqp.tags;

import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

class If extends Tag {

    /*
     * Standard if/else block
     */
    @Override
    public Object render(RenderContext context, LNode... nodes) {

        for (int i = 0; i < nodes.length - 1; i += 2) {

            Object exprNodeValue = nodes[i].render(context);
            LNode blockNode = nodes[i + 1];

            if (super.asBoolean(exprNodeValue)) {
                return blockNode.render(context);
            }
        }

        return null;
    }
}
