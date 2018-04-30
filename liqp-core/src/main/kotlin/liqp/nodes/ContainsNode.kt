package liqp.nodes

import com.google.common.collect.ImmutableSet
import liqp.context.LContext
import liqp.node.LNode

class ContainsNode(lhs: LNode, rhs: LNode) : ExpressionNode(lhs, rhs) {

  override fun render(context: LContext): Any? {

    val collection = lhs.render(context)
    val needle = rhs.render(context)

    if (context.isIterable(collection)) {
      val array = context.asIterable(collection)
      return ImmutableSet.copyOf(array).contains(needle)
    }

    val string = context.asString(collection)
    val needlStr = context.asString(needle)
    return when (needlStr) {
      null -> false
      else -> string?.contains(needlStr) ?: false
    }
  }
}
