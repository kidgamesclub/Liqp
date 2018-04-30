package liqp.tags

import liqp.ControlResult
import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

class Unless : LTag() {

  /**
   * Mirror of if statement
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {

    var i = 0
    while (i < nodes.size - 1) {

      val exprNodeValue = nodes[i].render(context)
      val blockNode = nodes[i + 1]

      if (context.isFalse(exprNodeValue)) {
        return blockNode.render(context)
      }
      i += 2
    }

    return ControlResult.NO_CONTENT
  }
}
