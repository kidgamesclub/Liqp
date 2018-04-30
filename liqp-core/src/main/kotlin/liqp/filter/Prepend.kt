package liqp.filter

import liqp.context.LContext

class Prepend : LFilter() {

  /**
   * (Object) append(input, string)
   *
   * add one string to another
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    context.run {
      val content = asString(value) ?: ""
      val prepend: String = asString(params[0]) ?: return content
      return prepend + content
    }
  }
}
