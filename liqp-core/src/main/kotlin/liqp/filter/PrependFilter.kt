package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class PrependFilter : LFilter() {

  /**
   * (Object) append(input, string)
   *
   * add one string to another
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      val content = asString(value) ?: ""
      val prepend: String = asString(params[0]) ?: return content
      return prepend + content
    }
  }
}
