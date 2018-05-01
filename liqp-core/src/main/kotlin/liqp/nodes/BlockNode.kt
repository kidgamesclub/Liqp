package liqp.nodes

import liqp.ControlResult
import liqp.ControlResult.BREAK
import liqp.ControlResult.CONTINUE
import liqp.ControlResult.NOOP
import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.node.LNode

data class BlockNode(override val children: List<LNode>) : LNode() {

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
        null -> {
          // shouldn't return null.
        }
        NOOP-> return value
        BREAK -> return value
        CONTINUE -> return value
        ControlResult.NO_CONTENT -> {}
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
        outputs.filter{it !is ControlResult}.forEach { output.append(it).checkSize() }
        return output.toString()
      }
    }
  }
}
