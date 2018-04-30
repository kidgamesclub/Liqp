package liqp.tags

import liqp.tag.LTag
import liqp.node.LNode
import liqp.context.LContext

class If : LTag() {

  /*
   * Standard if/else block
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    var i = 0
    while (i < nodes.size - 1) {

      val exprNodeValue = nodes[i].render(context)
      val blockNode = nodes[i + 1]

      if (context.isTrue(exprNodeValue)) {
        return blockNode.render(context)
      }
      i += 2
    }

    return null
  }
}
