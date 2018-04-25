package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;
import liqp.context.LContext;

public class Comment extends LTag {

    /*
     * Block tag, comments out the text in the block
     */
    @Override
    public Object render(LContext context, LNode... nodes) {
        return "";
    }
}
