package liqp.tags

import liqp.ControlResult
import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

class Comment : LTag() {

  /**
   * Block tag, comments out the text in the block
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    return ControlResult.NO_CONTENT
  }
}
