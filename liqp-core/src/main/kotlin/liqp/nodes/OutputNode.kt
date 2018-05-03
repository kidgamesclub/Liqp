package liqp.nodes

import liqp.context.LContext
import liqp.filter.FilterChain
import liqp.node.LNode

data class OutputNode(private val expr: Any?, val filters: List<FilterNode>) : LNode() {

  private val expression = expr as? LNode ?: AtomNode(expr)
  override val children: List<LNode> = listOf(expression) + filters

  override fun render(context: LContext): Any? {
    return if (filters.isEmpty()) {
      expression.render(context)
    } else {
      val filters = filters.map { it[context] }
      val filterChain = FilterChain(context, filters) {
        result = expression.render(context)
        result
      }
      filterChain.processFilters()
    }
  }
}
