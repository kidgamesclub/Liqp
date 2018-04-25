package liqp.filter

import liqp.context.LContext
import liqp.node.LNode

/**
 * Contains filter parameters that can be resolved when needed.  The instance ensures that the
 * params are resolved exactly once, and cached thereafter.
 */
data class FilterParams(private val context: LContext,
                        private val paramNodes: List<LNode?>) {

  val size
    get() = resolvedParams.size

  private val resolvedParams: List<Any?> by lazy {
    paramNodes
        .map { pnode ->
          when (pnode) {
            null -> null
            else -> pnode.render(context)
          }
        }
  }

  operator fun get(index: Int): Any? {
    return resolvedParams.getOrNull(index)
  }

}
