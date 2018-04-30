package liqp

import liqp.ControlResult.*
import liqp.context.LContext
import liqp.node.LNode

enum class ControlResult {
  CONTINUE,
  BREAK,
  NOOP,
  EMPTY,
  NO_CONTENT
}

val EMPTY_NODE = object : LNode() {
  override val children: Iterable<LNode> = emptyList()
  override fun render(context: LContext): Any? = EMPTY
}



