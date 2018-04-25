package liqp.node

import liqp.context.LContext

/**
 * Denotes a node in the AST the parse creates from the
 * input source.
 */
interface LNode {

  val children: Iterable<LNode>

  /**
   * Renders this AST.
   *
   * @param context
   * the context (variables) with which this
   * node should be rendered.
   *
   * @return an Object denoting the rendered AST.
   */
  fun render(context: LContext): Any?
}
