package liqp.nodes

import liqp.context.LContext
import liqp.node.LNode

class KeyValueNode(val key: String, val value: LNode) : LNode() {

  override val children = listOf(value)

  override fun render(context: LContext): Any {
    return mutableMapOf<Any, Any?>().apply {
      this[key] = value.render(context)
    }
  }
}
