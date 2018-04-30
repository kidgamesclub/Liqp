package liqp.nodes

import liqp.ComparisonResult.GREATER
import liqp.ComparisonResult.LESS
import liqp.ComparisonResult.NOOP
import liqp.ComparisonResult.NULL
import liqp.node.LNode

class NEqNode(lhs: LNode, rhs: LNode) : ComparisonNode(lhs, rhs) {
  override val matchingComparisons = setOf(NULL, LESS, GREATER, NOOP)
}
