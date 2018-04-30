package liqp.nodes

import liqp.ComparisonResult
import liqp.context.LContext
import liqp.node.LNode

abstract class ComparisonNode(
    lhs: LNode,
    rhs: LNode) : ExpressionNode(lhs, rhs) {

  abstract val matchingComparisons: Set<ComparisonResult>

  override fun render(context: LContext): Any {
    val a = lhs.render(context)
    val b = rhs.render(context)

    return context.compareTo(a, b) in matchingComparisons
  }
}
