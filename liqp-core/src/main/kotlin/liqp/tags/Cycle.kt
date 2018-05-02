package liqp.tags

import liqp.context.LContext
import liqp.node.LNode
import liqp.tag.LTag

const val CYCLE_PREPEND = "\"'"

class Cycle : LTag() {

  /**
   * Cycle is usually used within a loop to alternate
   * between values, like colors or DOM classes.
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    // The group-name is either the first token-expression, or if that is
    // null (indicating there is no name), give it the name [CYCLE_PREPEND] followed
    // by the number of expressions in the cycle-group.
    val groupName = nodes.firstOrNull()
        ?.executeOrNull(context)
        ?: CYCLE_PREPEND + (nodes.size -1)

    val elements = nodes.map { it.render(context) }
    val group = context[groupName, {CycleGroup(elements.size)}]
    return group.next(elements)
  }

  private class CycleGroup internal constructor(private val sizeFirstCycle: Int) {
    private var iterable:Iterable<out Any>

    internal fun next(elements: List<Any?>): Any {
      val next = elements.getOrNull(idx++)
      if (idx >= sizeFirstCycle) {
        idx = 0
      }

      return next ?: ""
    }
  }
}
