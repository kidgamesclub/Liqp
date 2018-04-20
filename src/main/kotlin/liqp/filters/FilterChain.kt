package liqp.filters

import liqp.nodes.RenderContext
import java.util.concurrent.atomic.AtomicReference


class FilterChain(val context: RenderContext,
                  val filters: List<FilterWithParams>,
                  val filterAction: (AtomicReference<Any?>)->Any?) {


  private val pointer:Iterator<FilterWithParams> = filters.reversed().iterator()
  private val result:AtomicReference<Any?> = AtomicReference()

  fun processFilters():Any? {
    filters.forEach {
      it.filter.doStartFilterChain(it.params,this, context, result)
    }
    continueChain()
    filters.forEach {
      it.filter.doFilterEnd(it.params, this, context, result)
    }
    return result.get()
  }

  fun continueChain():Any? {
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
