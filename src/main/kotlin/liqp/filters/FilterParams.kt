package liqp.filters

import liqp.nodes.LNode
import liqp.nodes.RenderContext
import java.util.concurrent.atomic.AtomicReference

/**
 * Interface that allows for nested filters to control lifecycle.
 */
class FilterParams(private val paramNodes: List<LNode?>) {

  private var resolvedParams: Array<Any?>? = null

  /**
   * Resolve the params as late as possible, do a double-check lock to ensure they are resolved once
   */
  fun resolve(context: RenderContext):Array<Any?> {
    if (resolvedParams == null) {
      synchronized(this) {
        if (resolvedParams == null) {
          this.resolvedParams = paramNodes
              .map { pnode ->
                when (pnode) {
                  null -> null
                  else -> pnode.render(context)
                }
              }
              .toTypedArray()
        }
      }
    }
    return resolvedParams!!
  }
}
