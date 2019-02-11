package liqp.nodes

import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.LESS
import liqp.node.LNode

class LtEqNode(lhs: LNode, rhs: LNode) : ComparisonNode(lhs, rhs) {
  override val matchingComparisons = setOf(EQUAL, LESS)
}
