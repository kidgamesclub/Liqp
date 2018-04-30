package liqp.nodes

import liqp.context.LContext
import liqp.filter.FilterInstance
import liqp.filter.FilterParams
import liqp.filter.LFilter
import liqp.filter.ResolvableFilterParams
import liqp.node.LNode

class FilterNode
constructor(val filter: LFilter, val params: List<LNode>) : LNode() {

  override val children: List<LNode> get() = params

  override fun render(context: LContext): Any = TODO()

  /**
   * Creates an instance of this filter for rendering.  This wrapper class allows for delayed resolution of
   * filter params, in case this filter is never invoked.
   */
  operator fun get(context: LContext): FilterInstance {
    return FilterInstance(filter, ResolvableFilterParams(context, params))
  }
}
