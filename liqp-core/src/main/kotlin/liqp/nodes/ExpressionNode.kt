package liqp.nodes

import liqp.node.LNode

abstract class ExpressionNode protected constructor(
    val lhs: LNode,
    val rhs: LNode) : LNode() {

  override val children: Iterable<LNode> get() = listOf(lhs, rhs)
}
