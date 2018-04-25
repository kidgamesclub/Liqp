package liqp.filter

import liqp.context.LContext

/**
 * Removes non-null values from lists, arrays, and maps.
 * Will also prune null keys from maps
 *
 * {{ [2,3,null,4,null] }} -> [2,3,4]
 */
class Compact : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
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
