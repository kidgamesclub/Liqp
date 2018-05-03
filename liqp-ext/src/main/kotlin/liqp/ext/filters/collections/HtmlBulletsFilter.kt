package liqp.ext.filters.collections

import liqp.context.LContext
import liqp.params.FilterParams
import liqp.filter.LFilter

class HtmlBulletsFilter : LFilter() {
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
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
