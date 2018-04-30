package liqp.filter

import liqp.context.LContext

typealias Slicer = (Int, Int) -> Any

/**
 * Returns a slice of an array or string.
 */
class Slice : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {

    context.run {
      val p1: Int = params[0] ?: throw IllegalArgumentException("liquid error: Invalid integer")
      val p2: Int = params[1] ?: 1

      val totalLength: Int

      val slicer: Slicer = when (isIterable(value)) {
        true -> {
          val iterable = asIterable(value).toList()
          totalLength = iterable.size
          { offset: Int, length: Int -> iterable.subList(offset, offset + length) }
        }
        false -> {
          val string = asString(value) ?: return null
          totalLength = string.length
          { offset: Int, length: Int -> string.substring(offset, length) }
        }
      }
      val offset: Int = when {
        p1 < 0 -> totalLength + p1
        else -> p1
      }
      val length = when {
        offset + p2 > totalLength -> totalLength - offset
        else -> p2
      }
      if (offset > totalLength || offset < 0) {
        return ""
      }

      return slicer(offset, length)
    }
  }
}
