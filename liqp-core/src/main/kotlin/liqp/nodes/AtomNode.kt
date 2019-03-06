@file:Suppress("UNCHECKED_CAST")

package liqp.nodes

import liqp.context.LContext
import liqp.node.LNode

class AtomNode(private val value: Any?) : LNode() {

  override val children: List<LNode> = emptyList()

  override fun render(context: LContext): Any? {
    return value
  }

  fun <X> get(): X {
    return value as X
  }
}
