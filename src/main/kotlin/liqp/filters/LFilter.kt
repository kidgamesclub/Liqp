package liqp.filters

import liqp.nodes.RenderContext
import java.util.concurrent.atomic.AtomicReference

/**
 * Interface for performing filter operations.
 */
interface LFilter {
  fun doFilter(params: FilterParams,
               chain: FilterChainPointer,
               context: RenderContext,
               result: AtomicReference<Any?>)

  fun doStartFilterChain(params: FilterParams,
                         chain: FilterChainPointer,
                         context: RenderContext,
                         result: AtomicReference<Any?>)

  fun doFilterAction(params: FilterParams,
                     chain: FilterChainPointer,
                     context: RenderContext,
                     result: AtomicReference<Any?>)

  fun doEndFilterChain(params: FilterParams,
                       chain: FilterChainPointer,
                       context: RenderContext,
                       result: AtomicReference<Any?>)
}
