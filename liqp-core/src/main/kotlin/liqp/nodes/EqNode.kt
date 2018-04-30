package liqp.nodes

import liqp.ComparisonResult
import liqp.context.LContext
import liqp.node.LNode

class EqNode(lhs: LNode, rhs: LNode) : ExpressionNode(lhs, rhs) {

  override fun render(context: LContext): Any {
    val a = lhs.render(context)
    val b = rhs.render(context)
    return context.areEqual(a, b)
  }
}
