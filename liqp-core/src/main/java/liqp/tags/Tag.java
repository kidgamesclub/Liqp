package liqp.tags;

import liqp.LValue;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

/**
 * Tags are used for the logic in a template.
 */
public abstract class Tag extends LValue {
    /**
     * The name of this tag.
     */
    public final String name;

    /**
     * Used for all package protected tags in the liqp.tags-package
     * whose name is their class name lower cased.
     */
    protected Tag() {
        this.name = this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Creates a new instance of a Tag.
     *
     * @param name
     *         the name of the tag.
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Renders this tag.
     *
     * @param context
     *         the context (variables) with which this
     *         node should be rendered.
     * @param nodes
     *         the nodes of this tag is created with. See
     *         the file `src/grammar/LiquidWalker.g` to see
     *         how each of the tags is created.
     *
     * @return an Object denoting the rendered AST.
     */
    public abstract Object render(RenderContext context, LNode... nodes);


}
