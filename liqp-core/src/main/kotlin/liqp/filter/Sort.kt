package liqp.filter

import liqp.context.LContext
import kotlin.collections.Map

class Sort : LFilter() {

  /**
   * sort(input, property = nil)
   *
   * Sort elements of the array provide optional property with
   * which to sort an array of hashes or drops
   */
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {

    val v = value ?: return null

    context.run {
      val asList: MutableList<Any> = asIterable(value).toMutableList()
      val property = asString(params[0])

      val list:MutableList<Comparable<Any>> = asComparableList(asList, property)
      list.sort()
      return list
    }
  }

  private fun asComparableList(array: MutableList<Any>, property: String?): MutableList<Comparable<Any>> {
    val list = mutableListOf<Comparable<Any>>()

    for (obj in array) {

      if (obj is kotlin.collections.Map<*, *> && property != null) {
        val asMap = obj as kotlin.collections.Map<String, Comparable<Any>>
        list.add(SortableMap(asMap.toMutableMap(), property) as Comparable<Any>)
      } else {
        list.add(obj as Comparable<Any>)
      }
    }

    return list
  }

  internal class SortableMap(val map: MutableMap<String, Comparable<Any>>, val property: String) :
      MutableMap<String, Comparable<Any>> by map,
      Comparable<SortableMap> {

    init {
      putAll(map)
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

      for ((key, value) in this) {
        builder.append(key).append(value)
      }

      return builder.toString()
    }
  }
}
