package liqp.nodes

import liqp.context.LContext
import liqp.node.LNode

class OrNode(lhs: LNode, rhs: LNode) : ExpressionNode(lhs, rhs) {
  override fun render(context: LContext): Any {
    return context.isTrue(lhs.render(context)) || context.isTrue(rhs.render(context))
  }
}
