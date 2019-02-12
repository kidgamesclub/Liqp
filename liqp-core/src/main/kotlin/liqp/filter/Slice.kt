package liqp.filter

import lang.exception.illegalArgument
import liqp.context.LContext
import liqp.params.FilterParams
import liqp.safeSlice
import liqp.safeSubstring

typealias Slicer = (Int) -> Any

/**
 * Returns a slice of an array or string.
 */
class Slice : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? = context {
    val p1:Int = params[0] ?: illegalArgument("liquid error: Invalid integer")
    val length: Int = params[1, 1]

    val totalLength: Int

    val slicer: Slicer = when (isIterable(value)) {
      true -> {
        val iterable = asIterable(value).toList()
        totalLength = iterable.size
        { offset: Int -> iterable.safeSlice(offset, offset + length) }
      }
      false -> {
        val string = asString(value) ?: return@context null
        totalLength = string.length
        { offset: Int -> string.safeSubstring(offset, offset + length) }
      }
    }
    val offset: Int = when {
      p1 < 0 -> totalLength + p1
      else -> p1
    }

    return@context slicer(offset)
  }
}
