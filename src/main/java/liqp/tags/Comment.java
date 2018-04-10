package liqp.tags;

import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

class Comment extends Tag {

    /*
     * Block tag, comments out the text in the block
     */
    @Override
    public Object render(RenderContext context, LNode... nodes) {
        return "";
    }
}
