package liqp.tags

import liqp.ControlResult
import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

class Break : LTag() {
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    return ControlResult.BREAK
  }
}
