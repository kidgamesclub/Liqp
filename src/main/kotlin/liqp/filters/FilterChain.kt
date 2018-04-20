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
                  private val filterAction: (AtomicReference<Any?>) -> Any?) : FilterChainPointer {

  private val data = mutableMapOf<String, Any?>()
  private val pointer: Iterator<FilterWithParams> = filters.reversed().iterator()
  private val result: AtomicReference<Any?> = AtomicReference()

  /**
   * Processes the filters and returns the result.
   */
  fun processFilters(): Any? {
    filters.forEach {
      it.filter.onStartChain(it.params, this, context, result)
    }
    continueChain()
    filters.forEach {
      it.filter.onEndChain(it.params, this, context, result)
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
      filters.forEach {
        it.filter.onFilterAction(it.params, this, context, result)
      }
    }
    return result.get()
  }

  @Suppress("UNCHECKED_CAST")
  override
  operator fun <I> get(key: String): I? {
    return data[key] as I
  }

  override operator fun set(key: String, value: Any?) {
    data[key] = value
  }

  override fun isFlagged(key: String): Boolean {
    return data.containsKey(key)
  }

  override fun flag(key: String) {
    data[key] = true
  }

  override fun unflag(key: String) {
    data.remove(key)
  }

  override fun withFlag(key:String, block:()->Unit) {
    if (isFlagged(key)) {
      block()
      unflag(key)
    }
  }
}
