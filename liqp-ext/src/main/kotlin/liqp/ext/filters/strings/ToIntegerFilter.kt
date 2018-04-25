package liqp.ext.filters.strings
import liqp.nodes.RenderContext

class ToIntegerFilter :Filter() {

  override fun apply(context: RenderContext, value: Any, vararg params: Any): Any {
    var ret = value

    if (value is String) {
      ret = Integer.parseInt(value)
    }

    return ret
  }
}
