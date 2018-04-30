package liqp.tags

import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

class Raw : LTag() {

  /**
   * temporarily disable tag processing to avoid syntax conflicts.
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    return nodes[0].render(context)
  }
}
