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

    val iterator = nodes.iterator()
    while (iterator.hasNext()) {
      val exprNodeValue = iterator.next().render(context)
      val blockNode = iterator.next()

      if (context.isFalse(exprNodeValue)) {
        return blockNode.render(context)
      }
    }

    return ControlResult.NO_CONTENT
  }
}
