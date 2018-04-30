package liqp.nodes

import liqp.context.LContext
import liqp.node.LNode
import lombok.Getter

class AttributeNode(val key: LNode, val value: LNode) : LNode() {

  override val children: List<LNode> get() = listOf(key, value)

  override fun render(context: LContext): Any? {
    return arrayOf(key.render(context), value.render(context))
  }
}
