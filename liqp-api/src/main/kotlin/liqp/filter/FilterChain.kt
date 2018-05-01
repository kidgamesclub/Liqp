package liqp.filter

import liqp.ControlResult.EMPTY
import liqp.ControlResult.NOOP
import liqp.ControlResult.NO_CONTENT
import liqp.context.LContext

/**
 * Invokes a set of filter around an "action", or render output.
 *
 * This ensures that the filter can manage the lifecycle of before/after the target content is
 * rendered, to support cases where state needs to be managed during rendering.
 */
class FilterChain(private val context: LContext,
                  private val filters: List<FilterInstance>,
                  private val filterAction: LContext.() -> Any?) : FilterChainPointer {

  private val data = mutableMapOf<String, Any?>()
  private val pointer: Iterator<FilterInstance> = filters.reversed().iterator()

  /**
   * Processes the filter and returns the result.
   */
  fun processFilters(): Any? {
    filters.forEach {
      it.filter.onStartChain(it.params, this, context)
    }
    continueChain()
    filters.forEach {
      it.filter.onEndChain(it.params, this, context)
    }
    return context.result
  }

  /**
   * Should normally only be invoked by the filter themselves.
   */
  override fun continueChain(): Any? {
    if (pointer.hasNext()) {
      val next = pointer.next()
      next.filter.doFilter(next.params, this, context)
    } else {
      val actionResult = context.filterAction()
      if (actionResult != NO_CONTENT) {
        context.result = actionResult
      }
      filters.forEach {
        val result = it.filter.onFilterAction(context, context.result, it.params)
        if (result != NO_CONTENT && result != EMPTY && result != NOOP) {
          context.result = result
        }
      }
    }
    return context.result
  }

  @Suppress("UNCHECKED_CAST")
  override operator fun <I> get(key: String): I? {
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

  override fun <I> with(key: String, block: (I) -> Unit) {
    if (isFlagged(key)) {
      val i: I = this[key]!!
      block(i)
      unflag(key)
    }
  }

  override fun <I> once(key: String, block: () -> I) {
    if (!isFlagged(key)) {
      this[key] = block()
    }
  }
}
