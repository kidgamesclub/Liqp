package liqp.ext.filters.strings
import liqp.nodes.RenderContext

class ToDoubleFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    return when(value) {
      is Number-> value.toDouble()
      else-> value.toString().toDoubleOrNull() ?: 0
    }
  }
}
