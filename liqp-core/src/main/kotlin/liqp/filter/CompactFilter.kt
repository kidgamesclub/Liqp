package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams

/**
 * Removes non-null values from lists, arrays, and maps.
 * Will also prune null keys from maps
 *
 * {{ [2,3,null,4,null] }} -> [2,3,4]
 */
class CompactFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    context.run {
      return when (value) {
        is kotlin.collections.Map<*, *> -> value.filterKeys { it != null }.filterValues { it != null }
        is Iterable<*> -> value.filterNotNull()
        is Array<*> -> value.filterNotNull()
        else -> value
      }
    }
  }
}
