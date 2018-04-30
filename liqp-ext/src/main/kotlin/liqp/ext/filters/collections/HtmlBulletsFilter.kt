package liqp.ext.filters.collections

import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import liqp.nodes.RenderContext

class HtmlBulletsFilter : LFilter() {
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    return when (value) {
      is Iterable<*> -> {
        val builder = StringBuilder()

        builder.append("<ul>")
        params.map({ it.toString() })
            .filter({ it.isNotEmpty() })
            .forEach { item -> builder.append("<li>").append(item).append("</li>") }
        builder.append("</ul>")
      }
      else -> value ?: ""
    }
  }
}
