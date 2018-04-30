package liqp.nodes

import liqp.ComparisonResult.GREATER
import liqp.node.LNode

class GtNode(lhs: LNode, rhs: LNode) : ComparisonNode(lhs, rhs) {
  override val matchingComparisons = setOf(GREATER)

}
