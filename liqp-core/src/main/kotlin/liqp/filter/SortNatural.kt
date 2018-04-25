package liqp.filter

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator
import liqp.context.LContext

class SortNatural : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (!super.isArray(value)) {
      return value
    }

    val array = context.asIterable(value)
    val list = ArrayList(Arrays.asList<Any>(*array!!))

    Collections.sort<Any>(list, { o1, o2 -> o1.toString().compareTo(o2.toString(), ignoreCase = true) } as Comparator<*>)

    return list.toTypedArray()
  }
}
