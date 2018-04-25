package liqp.filter

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import liqp.context.LContext

class Reverse : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return context.asIterable(value).reversed()
  }
}
