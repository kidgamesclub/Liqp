package liqp.nodes;

import java.util.List;

/**
 * Denotes a node in the AST the parse creates from the
 * input source.
 */
public interface LNode {

    /**
     * Renders this AST.
     *
     * @param context
     *         the context (variables) with which this
     *         node should be rendered.
     *
     * @return an Object denoting the rendered AST.
     */
    Object render(RenderContext context);

    List<LNode> children();
}
