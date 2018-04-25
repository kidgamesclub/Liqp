package liqp.ext.filters.colors
import liqp.nodes.RenderContext


import java.awt.Color

class ToRgbFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    return try {
      val decoded = when(value) {
        is Color-> value
        else-> Color.decode(value.toString())
      }
      "${decoded.red},${decoded.green},${decoded.blue}"
    } catch (e: Exception) {
      value
    }
  }
}
