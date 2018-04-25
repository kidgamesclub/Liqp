package liqp.filter

import java.util.ArrayList
import liqp.context.LContext

class Map : LFilter() {

  /*
     * map(input, property)
     *
     * map/collect on a given property
     */
  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (value == null) {
      return ""
    }

    val list = ArrayList<Any>()

    val array = context.asIterable(value)

    val key = super.asString(super.get(0, params))

    for (obj in array!!) {

      val map = obj as Map<*, *>

      val `val` = map.get(key)

      if (`val` != null) {
        list.add(`val`)
      }
    }

    return list.toTypedArray()
  }
}
