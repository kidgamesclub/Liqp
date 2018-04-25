package liqp.filter

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import liqp.context.LContext

class Sort : LFilter() {

  /*
     * sort(input, property = nil)
     *
     * Sort elements of the array provide optional property with
     * which to sort an array of hashes or drops
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (value == null) {
      return ""
    }

    if (!super.isArray(value)) {
      throw RuntimeException("cannot sort: $value")
    }

    val array = context.asIterable(value)
    val property = if (params.size == 0) null else super.asString(params[0])

    val list = asComparableList(array!!, property)

    Collections.sort<Comparable>(list)

    return if (property == null)
      list.toTypedArray()
    else
      list.toTypedArray<SortableMap>()
  }

  private fun asComparableList(array: Array<Any>, property: String?): List<Comparable<*>> {

    val list = ArrayList<Comparable<*>>()

    for (obj in array) {

      if (obj is Map<*, *> && property != null) {
        list.add(SortableMap(obj as Map<String, Comparable<*>>, property))
      } else {
        list.add(obj as Comparable<*>)
      }
    }

    return list
  }

  internal class SortableMap(map: Map<String, Comparable<*>>, val property: String) : HashMap<String, Comparable<*>>(), Comparable<SortableMap> {

    init {
      super.putAll(map)
    }

    override fun compareTo(that: SortableMap): Int {

      val thisValue = this[property]
      val thatValue = that[property]

      if (thisValue == null || thatValue == null) {
        throw RuntimeException("Liquid error: comparison of Hash with Hash failed")
      }

      return thisValue.compareTo(thatValue)
    }

    override fun toString(): String {
      val builder = StringBuilder()

      for ((key, value) in super) {
        builder.append(key).append(value)
      }

      return builder.toString()
    }
  }
}
