package liqp.traverse

import liqp.Template
import liqp.node.LNode

typealias LNodeVisitor = (LNode, List<LNode>) -> Boolean

class LNodeWalker(vararg visitors: LNodeVisitor) {
  private val visitors = listOf(*visitors)

  fun walkTree(template: Template) {
    walkNode(template.rootNode, listOf(), visitors)
  }

  private fun walkNode(node: LNode, parents: List<LNode>, visitors: List<LNodeVisitor>) {
    visitors.forEach { nodeVisitor -> nodeVisitor(node, parents) }

    node.children
        .forEach { walkNode(it, parents + node, visitors) }
  }
}

inline fun <reified N : LNode> Template.iterator(): Iterable<N> {
  val list = mutableListOf<N>()

  this.walkNodes { node: N ->
    list.add(node)
  }

  return list
}

inline fun <reified N : LNode> Template.treeIterator(): Iterable<Pair<N, List<LNode>>> {
  val list = mutableListOf<Pair<N, List<LNode>>>()

  this.walkNodeTree { node: N, parents ->
    list.add(node to parents)
  }

  return list.toList()
}

fun Template.walkNodes(vararg visitor: FilteredLNodeVisitor) {
  LNodeWalker(*visitor).walkTree(this)
}

inline fun <reified N : LNode> Template.walkNodeTree(crossinline visitor: (N, List<LNode>) -> Unit) {
  LNodeWalker(FilteredLNodeVisitor(
      nodeVisitor = { node, parents -> visitor(node as N, parents) },
      nodeFilter = { node, _ -> N::class.isInstance(node) }
  )).walkTree(this)
}

inline fun <reified N : LNode> Template.walkNodes(crossinline visitor: (N) -> Unit) {
  LNodeWalker(FilteredLNodeVisitor(
      nodeVisitor = { node, _ -> visitor(node as N) },
      nodeFilter = { node, _ -> N::class.isInstance(node) }
  )).walkTree(this)
}



