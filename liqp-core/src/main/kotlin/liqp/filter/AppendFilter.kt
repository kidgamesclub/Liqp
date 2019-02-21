package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * {{ foo.bar | append:'john' }}
 *
 * add one string to another
 */
class AppendFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      val content = asString(value) ?: ""
      val append: String = asString(params[0]) ?: return content
      return content + append
    }
  }
}
