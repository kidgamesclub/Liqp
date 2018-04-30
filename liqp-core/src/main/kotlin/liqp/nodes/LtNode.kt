package liqp.nodes

import liqp.ComparisonResult.LESS
import liqp.node.LNode
import lombok.Getter

@Getter
class LtNode(lhs: LNode, rhs: LNode) : ComparisonNode(lhs, rhs) {
  override val matchingComparisons = setOf(LESS)
}
