package liqp.nodes

import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.GREATER
import liqp.node.LNode

class GtEqNode(lhs: LNode, rhs: LNode) : ComparisonNode(lhs, rhs) {
  override val matchingComparisons = setOf(EQUAL, GREATER)
}
