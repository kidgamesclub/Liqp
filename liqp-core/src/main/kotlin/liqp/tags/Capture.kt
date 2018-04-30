package liqp.tags

import liqp.ControlResult
import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

class Capture : LTag() {

  /**
   * Block tag that captures text into a variable
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {

    val id:String = nodes[0].execute(context)
    val block = nodes[1]

    // Capture causes variable to be saved "globally"
    context[id] = block.render(context)

    return ControlResult.NO_CONTENT
  }
}
