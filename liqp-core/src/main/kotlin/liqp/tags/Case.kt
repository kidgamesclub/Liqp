package liqp.tags

import liqp.ControlResult
import liqp.LogicResult.TRUE
import liqp.context.LContext
import liqp.node.LNode
import liqp.nodes.BlockNode
import liqp.tag.LTag

class Case : LTag() {

  /**
   * Block tag, its the standard case...when block
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    //        ^(CASE condition           var
    //            ^(WHEN term+ block)    1,2,3  b1
    //            ^(ELSE block?))               b2
    val condition = nodes[0].render(context)

    val iterator = nodes.iterator()
    while (iterator.hasNext()) {
      val node = iterator.next()
      if (!iterator.hasNext() && node is BlockNode) {
        // this must be the trailing (optional) else-block
        return node.render(context)
      } else {
        // Iterate through the list of terms (of which we do not know the size):
        // - term (',' term)*
        // - term ('or' term)*
        // and stop when we encounter a BlockNode
        while (node !is BlockNode && iterator.hasNext()) {
          val whenExpressionValue = node.render(context)
          if (context.areEqual(condition, whenExpressionValue) == TRUE) {
            return node.render(context)
          }
        }
      }
    }

    return ControlResult.NO_CONTENT
  }
}
