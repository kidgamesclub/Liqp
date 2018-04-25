package liqp

import liqp.context.LContext
import liqp.node.LNode

class NoAction

val noAction = NoAction()


class LValue {
  companion object {

    @JvmStatic
    val NO_CONTENT = noAction

    @JvmStatic
    val EMPTY_NODE = object:LNode {
      override val children: Iterable<LNode> = emptyList()
      override fun render(context: LContext): Any? = null
    }

  }
}

