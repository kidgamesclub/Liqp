package liqp.nodes

import liqp.ComparisonResult
import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.LESS
import liqp.context.LContext
import liqp.node.LNode
import lombok.Getter

class LtEqNode(lhs: LNode, rhs: LNode) : ComparisonNode(lhs, rhs) {
  override val matchingComparisons = setOf(EQUAL, LESS)
}
