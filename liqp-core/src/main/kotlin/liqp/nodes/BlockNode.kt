package liqp.nodes

import liqp.ControlResult
import liqp.ControlResult.*
import liqp.context.LContext
import liqp.node.LNode
import liqp.node.LValue.BREAK
import liqp.node.LValue.CONTINUE
import liqp.exceptions.LiquidRenderingException

data class BlockNode(override val children: List<LNode>) : LNode {

  fun withChildNode(node: LNode): BlockNode {
    return this.copy(children = this.children + node)
  }

  override fun render(context: LContext): Any? {
    val maxSize = context.maxSizeRenderedString
    fun CharSequence.checkSize() {
      if (this.length > maxSize) throw LiquidRenderingException("rendered content too large: $maxSize")
    }

    val outputs = mutableListOf<Any>()
    for (node in children) {
      val value: Any? = node.render(context)
      when (value) {
        null -> null
        NOOP-> return value
        BREAK -> return value
        CONTINUE -> return value
        is Iterable<*> -> outputs.addAll(value.filterNotNull())
        is Array<*> -> outputs.addAll(value.filterNotNull())
        else -> outputs.add(value)
      }
    }

    val nonString = outputs.count { it !is String }

    return when {
      nonString == 1 && outputs.size == 1 -> outputs[0]
      else -> {
        val output = StringBuilder()
        outputs.forEach { output.append(it).checkSize() }
        return output.toString()
      }
    }
  }

  override fun children(): List<LNode> {
    return children
  }
}
