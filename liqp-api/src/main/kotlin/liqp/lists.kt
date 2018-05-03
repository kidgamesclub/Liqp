package liqp

/**
 * Creates a subList that adjusts from and to to boundaries of the list.
 */
fun <T> List<T>.safeSlice(from: Int, to: Int): Iterable<T> {
  val size = this.size
  val start = from.coerceIn(0, size)
  val end = to.coerceIn(start, size)
  return this.slice(start until end)
}

fun Collection<*>.asSingle(): Any? {
  return when (this.size) {
    1 -> this.iterator().next()
    else -> this
  }
}
