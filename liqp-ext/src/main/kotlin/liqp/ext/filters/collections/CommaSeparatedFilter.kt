package liqp.ext.filters.collections

import liqp.nodes.RenderContext

class CommaSeparatedFilter : Filter() {
  override fun apply(context: RenderContext, value: Any?, vararg params: Any): Any? {
    return when(value) {
      is Iterable<*>-> value.joinToString(separator = ", ")
      else-> value
    }
  }
}
