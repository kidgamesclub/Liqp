package liqp.tag

import liqp.context.LContext
import liqp.node.LNode
import liqp.toSnakeCase

/**
 * Tags are used for the logic in a template.
 */
abstract class LTag(vararg name: String) {

  val names: Iterable<String> = when {
    name.isEmpty() -> listOf(this.toSnakeCase().removeSuffix("_tag"))
    else -> name.asIterable()
  }

  val name = names.first()
  /**
   * Renders this tag.
   *
   * @param context
   * the context (variables) with which this
   * node should be rendered.
   * @param nodes
   * the nodes of this tag is created with. See
   * the file `src/grammar/LiquidWalker.g` to see
   * how each of the tags is created.
   *
   * @return an Object denoting the rendered AST.
   */
  abstract fun render(context: LContext, vararg nodes: LNode): Any?

  open fun render(context: LContext, nodes: List<LNode>): Any? {
    return render(context, *nodes.toTypedArray())
  }
}
