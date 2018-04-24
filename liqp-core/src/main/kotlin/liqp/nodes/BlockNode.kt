package liqp.nodes

import liqp.LValue.BREAK
import liqp.LValue.CONTINUE

import java.util.ArrayList
import lombok.Getter

data class BlockNode(private val children: List<LNode>) : LNode {

  fun withChildNode(node: LNode): BlockNode {
    return this.copy(children = this.children + node)
  }

  override fun render(context: RenderContext): Any? {

    val outputs = mutableListOf<Any>()
    for (node in children) {
      val value: Any? = node.render(context)

      when (value) {
        null -> {
        }
        BREAK -> return value
        CONTINUE -> return value
        is Iterable<*> -> {
          outputs.addAll(value.filterNotNull())
        }
        else -> outputs.add(value)
      }
    }

    var count = 0
    for (output in outputs) {
      if (output is String && output.trim().isNotEmpty()) {
        count++
      } else if (output !is String) {
        count++
      }
      if(count > 1) {
        break
      }
    }

    return when(count < 2) {
      true-> outputs.firstOrNull()
      else-> {
        val builder = StringBuilder()
        outputs.forEach {
          builder.append(it.toString())
          if (builder.length > context.maxSizeRenderedString) {
            throw RuntimeException("rendered string exceeds " + context.maxSizeRenderedString)
          }
        }
        builder.toString()
      }
    }
  }

  override fun children(): List<LNode> {
    return children
  }
}
