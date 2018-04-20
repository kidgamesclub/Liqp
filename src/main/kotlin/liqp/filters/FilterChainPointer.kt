package liqp.filters

/**
 * An interface that only exposes the part of a {@link FilterChain} relevant to the filters
 * themselves.
 */
interface FilterChainPointer {

  /**
   * Causes the filter chain to advance to the next filter in the chain.  Returns the current
   * resolved value, if any.
   */
  fun continueChain(): Any?
}

