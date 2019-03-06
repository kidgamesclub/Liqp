package liqp.filter

import lang.string.splitting
import liqp.context.LContext
import liqp.lookup.Property
import liqp.params.FilterParams
import liqp.resolve

class MapFilter : LFilter() {

  /**
   * map(input, property)
   *
   * map/collect on a given property
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {

    val v = value ?: return null
    val key: Any? = params[0]

    return when (v) {
      is kotlin.collections.Map<*, *> -> listOf(v[key]).filterNotNull()
      else -> {
        context.asIterable(v).mapNotNull {
          "$key".splitting('.')
              .map { Property(it) }
              .resolve(it, context)
        }
      }
    }
  }
}
