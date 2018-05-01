package liqp.tags

import liqp.ControlResult
import liqp.LogicResult.TRUE
import liqp.context.LContext
import liqp.find
import liqp.node.LNode
import liqp.nodes.BlockNode
import liqp.tag.LTag

class Case : LTag() {

  /**
   * Block tag, its the standard case...when block
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    val iterator = nodes.iterator()
    if (!iterator.hasNext()) {
      return ControlResult.NO_CONTENT
    }
    //        ^(CASE condition           var
    //            ^(WHEN term+ block)    1,2,3  b1
    //            ^(ELSE block?))               b2
    val subject = iterator.next().render(context)

    while (iterator.hasNext()) {
      var node = iterator.next()
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
          if (context.areEqual(subject, whenExpressionValue) == TRUE) {
            val nextBlockNode = iterator.find {it is BlockNode}
            return nextBlockNode.render(context)
          } else {
            node = iterator.next()
          }
        }
      }
    }

    return ControlResult.NO_CONTENT
  }
}
