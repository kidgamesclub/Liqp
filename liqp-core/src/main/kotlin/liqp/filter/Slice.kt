package liqp.filter

import java.util.Arrays
import liqp.context.LContext

class Slice : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    super.checkParams(params, 1, 2)

    if (!super.canBeInteger(params[0])) {
      throw RuntimeException("Liquid error: invalid integer")
    }

    var array: Array<Any>? = null
    var string: String? = null
    var offset = super.asNumber(params[0]).toInt()
    var length = 1
    val totalLength: Int

    if (super.isArray(value)) {
      array = context.asIterable(value)
      totalLength = array!!.size
    } else {
      string = super.asString(value)
      totalLength = string.length
    }

    if (params.size > 1) {

      if (!super.canBeInteger(params[1])) {
        throw RuntimeException("Liquid error: invalid integer")
      }

      length = super.asNumber(params[1]).toInt()
    }

    if (offset < 0) {
      offset = totalLength + offset
    }

    if (offset + length > totalLength) {
      length = totalLength - offset
    }

    if (offset > totalLength || offset < 0) {
      return ""
    }

    return if (array == null)
      string!!.substring(offset, offset + length)
    else
      Arrays.copyOfRange(array, offset, offset + length)
  }
}
