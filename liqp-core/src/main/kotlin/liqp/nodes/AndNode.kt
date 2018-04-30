package liqp.nodes

import liqp.context.LContext
import liqp.node.LNode
import lombok.Getter

class AndNode(lhs: LNode, rhs: LNode) : ExpressionNode(lhs, rhs) {

  override fun render(context: LContext): Any? {
    return context.isTrue(lhs.render(context))
        && context.isTrue(rhs.render(context))
  }
}
