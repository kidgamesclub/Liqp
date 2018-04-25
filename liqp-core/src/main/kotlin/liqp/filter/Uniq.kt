package liqp.filter

import java.util.Arrays
import java.util.LinkedHashSet
import liqp.context.LContext

class Uniq : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    if (!super.isArray(value)) {
      return value
    }

    val set = LinkedHashSet(Arrays.asList<Any>(*context.asIterable(value)!!))

    return set.toTypedArray()
  }
}
