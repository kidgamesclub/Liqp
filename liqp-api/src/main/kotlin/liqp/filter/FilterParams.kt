package liqp.filter

import liqp.context.LContext
import liqp.node.LNode

/**
 * Contains filter parameters that can be resolved when needed.  The instance ensures that the
 * params are resolved exactly once, and cached thereafter.
 */
data class FilterParams(private val context: LContext,
                        private val paramNodes: List<LNode?>) : Iterable<Any?> {

  val size get() = resolvedParams.size

  val resolvedParams: List<Any?> by lazy {
    paramNodes
        .map { pnode ->
          when (pnode) {
            null -> null
            else -> pnode.render(context)
          }
        }
  }

  inline operator fun <reified T : Any> get(index: Int): T? {
    return resolvedParams.getOrNull(index) as T?
  }

  inline operator fun <reified T : Any> get(index: Int, default:T): T {
    return resolvedParams.getOrNull(index) as T? ?: default
  }

  override fun iterator(): Iterator<Any?> = resolvedParams.iterator()
}
