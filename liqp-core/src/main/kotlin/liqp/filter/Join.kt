package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

class Join : LFilter() {

  /**
   * join(input, glue = ' ')
   *
   * Join elements of the array with certain character between them
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    val joiner: String = params[0] ?: " "
    return context.asIterable(value).joinToString(separator = joiner)
  }
}
