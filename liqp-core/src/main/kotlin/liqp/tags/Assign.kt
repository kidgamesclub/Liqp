package liqp.tags

import liqp.ControlResult
import liqp.context.LContext
import liqp.filter.FilterChain
import liqp.filter.FilterInstance
import liqp.filter.FilterParams
import liqp.node.LNode
import liqp.nodes.FilterNode
import liqp.tag.LTag

class Assign : LTag() {

  /**
   * Assigns some value to a variable
   */
  override fun render(context: LContext, vararg nodes: LNode): Any? {
    val filters = nodes
        .filter { node -> node is FilterNode }
        .map { node -> node as FilterNode }
        .map { node -> FilterInstance(node.filter, FilterParams(context, node.params)) }
    val chain = FilterChain(context, filters) {
      val expression = nodes[1]
      result = expression.render(context)
      ControlResult.NO_CONTENT
    }

    val finalValue = chain.processFilters()
    val id = nodes[0].render(context).toString()
    // Assign causes variable to be saved "globally"
    context.setRoot(id, finalValue!!)
    return null
  }
}
