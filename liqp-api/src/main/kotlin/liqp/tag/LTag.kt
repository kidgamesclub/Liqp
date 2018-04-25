package liqp.tag

import liqp.context.LContext
import liqp.node.LNode
import liqp.toSnakeCase

/**
 * Tags are used for the logic in a template.
 */
abstract class LTag(name:String? = null) {
  val name = name ?: this.toSnakeCase()

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
}
