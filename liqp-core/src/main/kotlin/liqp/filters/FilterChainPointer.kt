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

  /**
   * Retrieves data stored for this filter chain.  Performs an optimistic cast
   */
  @Suppress("UNCHECKED_CAST")
  operator fun <I> get(key: String): I?

  /**
   * Sets a value for this filter chain
   */
  operator fun set(key: String, value: Any?)

  /**
   * Checks a flag for this filter chain
   */
  fun isFlagged(key: String): Boolean

  /**
   * Sets a flag for this filter chain to true
   */
  fun flag(key: String)

  /**
   * Sets a flag to false (same as removing it)
   */
  fun unflag(key: String)

  fun <I> with(key: String, block: (I) -> Unit)

  fun <I> once(key: String, block: () -> I)
}

