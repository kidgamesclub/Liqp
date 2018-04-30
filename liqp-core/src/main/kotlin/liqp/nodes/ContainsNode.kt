package liqp.nodes

import com.google.common.collect.ImmutableSet
import java.util.Arrays
import liqp.context.LContext
import liqp.node.LNode
import lombok.Getter

class ContainsNode(lhs: LNode, rhs: LNode) : ExpressionNode(lhs, rhs) {

  override fun render(context: LContext): Any? {

    val collection = lhs.render(context)
    val needle = rhs.render(context)

    if (context.isIterable(collection)) {
      val array = context.asIterable(collection)
      return ImmutableSet.copyOf(array).contains(needle)
    }

    val string = context.asString(collection)
    return string?.contains(context.asString(needle)!!) ?: false
  }
}
