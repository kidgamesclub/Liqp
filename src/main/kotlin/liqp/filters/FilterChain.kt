package liqp.filters

import liqp.nodes.RenderContext
import java.util.concurrent.atomic.AtomicReference

/**
 * Invokes a set of filters around an "action", or render output.
 *
 * This ensures that the filters can manage the lifecycle of before/after the target content is
 * rendered, to support cases where state needs to be managed during rendering.
 */
class FilterChain(private val context: RenderContext,
                  private val filters: List<FilterWithParams>,
                  private val filterAction: (AtomicReference<Any?>) -> Any?): FilterChainPointer {

  private val pointer: Iterator<FilterWithParams> = filters.reversed().iterator()
  private val result: AtomicReference<Any?> = AtomicReference()

  /**
   * Processes the filters and returns the result.
   */
  fun processFilters(): Any? {
    filters.forEach {
      it.filter.doStartFilterChain(it.params, this, context, result)
    }
    continueChain()
    filters.forEach {
      it.filter.doFilterEnd(it.params, this, context, result)
    }
    return result.get()
  }

  /**
   * Should normally only be invoked by the filters themselves.
   */
  override fun continueChain(): Any? {
    if (pointer.hasNext()) {
      val next = pointer.next()
      next.filter.doFilter(next.params, this, context, result)
    } else {
      val actionResult = filterAction(result)
      if (actionResult != null) {
        result.set(actionResult)
      }
    }
    return result.get()
  }
}
