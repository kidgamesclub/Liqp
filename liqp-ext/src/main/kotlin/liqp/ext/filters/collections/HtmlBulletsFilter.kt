package liqp.ext.filters.collections

import liqp.nodes.RenderContext

class HtmlBulletsFilter : Filter() {
  override fun apply(context: RenderContext?, value: Any?, vararg params: Any?): Any {
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
