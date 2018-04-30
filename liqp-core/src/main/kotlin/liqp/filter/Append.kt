package liqp.filter

import liqp.context.LContext

/**
 * {{ foo.bar | append:'john' }}
 *
 * add one string to another
 */
class Append : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    context.run {
      val content = asString(value) ?: ""
      val append: String = asString(params[0]) ?: return content
      return content + append
    }
  }
}
