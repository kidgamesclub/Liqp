package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;
import liqp.context.LContext;

public class Unless extends LTag {

    /*
     * Mirror of if statement
     */
    @Override
    public Object render(LContext context, LNode... nodes) {

        for (int i = 0; i < nodes.length - 1; i += 2) {

            Object exprNodeValue = nodes[i].render(context);
            LNode blockNode = nodes[i + 1];

            if (context.isFalse(exprNodeValue)) {
                return blockNode.render(context);
            }
        }

        return "";
    }
}
