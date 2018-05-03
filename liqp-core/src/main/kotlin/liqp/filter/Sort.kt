package liqp.filter

import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.lookup.isNullAccessor
import liqp.params.FilterParams

typealias Sorter = Comparator<Any?>

class Sort : LFilter() {

  /**
   * sort(input, property = nil)
   *
   * Sort elements of the array provide optional property with
   * which to sort an array of hashes or drops
   */
  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {

    val v = value ?: return null

    context.run {
      val list: List<Any?> = asIterable(value).toList()
      val property = asString(params[0])

      val sorter:Sorter = when (property) {
        null-> Comparator {a:Any?, b:Any?-> context.compareTo(a, b).toInt() }
        else-> Comparator{a:Any?, b:Any?->
          val aVal = a
              ?.run{context.getAccessor(a, property)}
              ?.apply { if(this.isNullAccessor()) throw LiquidRenderingException("Property $property not found for ${a!!::class.java}") }
              ?.invoke(a)
          val bVal = b
              ?.run{context.getAccessor(b, property)}
              ?.apply { if(this.isNullAccessor()) throw LiquidRenderingException("Property $property not found for ${a!!::class.java}") }
              ?.invoke(b)
          context.compareTo(aVal, bVal).toInt()
        }
      }

      return list.sortedWith(sorter)
    }
  }
}
