package liqp.filter

import liqp.context.LContext

class Join : LFilter() {

  /**
   * join(input, glue = ' ')
   *
   * Join elements of the array with certain character between them
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    val joiner: String = params[0] ?: " "
    return context.asIterable(value).joinToString(separator = joiner)
  }
}
