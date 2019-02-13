package liqp.filter

import liqp.ControlResult
import liqp.context.LContext
import liqp.params.FilterParams
import liqp.params.ResolvedFilterParams
import liqp.params.emptyParams
import liqp.toSnakeCase

/**
 * Interface for performing filter lifecycle operations.  Instead of simply formatting the output, some filter may
 * want to communicate with other filter in the chain (maybe filter of the same type), and/or hijack the output
 * completely.
 *
 * Output markup takes filter. Filters are simple methods. The first parameter is always the output of the left side of
 * the filter. The return value of the filter will be the new left value when the next filter is run. When there are no
 * more filter, the template will receive the resulting string.
 * <p/>
 * -- https://github.com/Shopify/liquid/wiki/Liquid-for-Designers
 */
abstract class LFilter(name: String? = null) {

  open val name = name ?: this.toSnakeCase().removeSuffix("_filter")

  open fun doFilter(params: FilterParams,
                    chain: FilterChainPointer,
                    context: LContext) {
    chain.continueChain()
  }

  open fun onStartChain(params: FilterParams,
                        chain: FilterChainPointer,
                        context: LContext) {
  }

  open fun onFilterAction(context: LContext,
                          value: Any?,
                          params: FilterParams): Any? {
    return ControlResult.NO_CONTENT
  }

  open fun onEndChain(params: FilterParams,
                      chain: FilterChainPointer,
                      context: LContext) {
  }

  open fun onFilterAction(context: LContext, value: Any?, vararg params: Any?): Any? {
    val filterParams: FilterParams = when {
      params.isEmpty() -> emptyParams
      params.size == 1 && params[0] is FilterParams -> params[0] as FilterParams
      else -> ResolvedFilterParams(*params)
    }
    return onFilterAction(context, value, filterParams)
  }

  fun get(i: Int, params: FilterParams): Any? {
    return params[i]
  }
}

