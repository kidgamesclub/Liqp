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
    val groupNameEnd = nodes.firstOrNull()
        ?.executeOrNull(context)
        ?: CYCLE_PREPEND+(nodes.size - 1)

    val groupName = CYCLE_PREPEND + groupNameEnd
    val elements = nodes
        .slice(1..nodes.lastIndex)
        .map { it.render(context) }
    val existing:CycleGroup? = context[groupName]
    val group = when  {
      existing == null-> CycleGroup(elements)
      existing.size != elements.size-> CycleGroup(elements)
      else-> existing
    }
    context[groupName] = group
    return group.next()
  }

  private class CycleGroup(internal val elements: List<Any?>) : Iterator<Any> {
    private var iterator: Iterator<Any?> = elements.iterator()

    override fun hasNext(): Boolean = true

    override fun next(): Any {
      if (!iterator.hasNext()) {
        iterator = elements.iterator()
      }

      return iterator.next() ?: ""
    }

    val size get() = elements.size
  }
}
