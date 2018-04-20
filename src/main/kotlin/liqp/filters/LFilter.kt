package liqp.filters

import liqp.nodes.RenderContext
import java.util.concurrent.atomic.AtomicReference

interface LFilter {
  fun doFilter(params: FilterParams,
               chain: FilterChain,
               context: RenderContext,
               result: AtomicReference<Any?>)

  fun doStartFilterChain(params: FilterParams,
                         chain: FilterChain,
                         context: RenderContext,
                         result: AtomicReference<Any?>)

  fun doFilterEnd(params: FilterParams,
                  chain: FilterChain,
                  context: RenderContext,
                  result: AtomicReference<Any?>)
}
