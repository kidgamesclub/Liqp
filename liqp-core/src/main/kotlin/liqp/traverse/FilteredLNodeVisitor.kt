package liqp.traverse

import liqp.node.LNode

class FilteredLNodeVisitor(private val nodeVisitor: (LNode, List<LNode>) -> Unit,
                           private val nodeFilter: (LNode, List<LNode>) -> Boolean = { _, _ -> true }) : LNodeVisitor {

  /**
   * Visits a node, and determines whether to continue processing the child nodes
   * @param node The node being inspected
   * @param parents The parent nodes for the node being inspected
   * @return true/false Whether the controller should continue processing child nodes
   */
  override fun invoke(node: LNode, parents: List<LNode>): Boolean {
    return if (nodeFilter(node, parents)) {
      nodeVisitor(node, parents)
      true
    } else {
      false
    }
  }
}
