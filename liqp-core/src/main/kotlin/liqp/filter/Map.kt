package liqp.filter

import liqp.context.LContext
import java.util.*

class Map : LFilter() {

  /**
   * map(input, property)
   *
   * map/collect on a given property
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {

    val v = value ?: return null
    val key:Any? = params[0]

    return when (v) {
      is kotlin.collections.Map<*,*> -> listOf(v[key]).filterNotNull()
      else-> {
        context.asIterable(v)
            .filter { it is kotlin.collections.Map<*,*> }
            .mapNotNull { (it as kotlin.collections.Map<*,*>)[key] }
      }
    }
  }
}
