package liqp.filter

import liqp.context.LContext

typealias NumberOp = LContext.(Any?, Any?) -> Any?

abstract class NumberOpFilter(private val op: NumberOp) : LFilter() {
  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {
    var result = value
    for (param in params) {
      result = context.op(result, param)
    }
    return result
  }
}

class DividedBy : NumberOpFilter(LContext::div)
class Times :  NumberOpFilter(LContext::mult)
class Plus :  NumberOpFilter(LContext::add)
class Minus :  NumberOpFilter(LContext::subtract)
