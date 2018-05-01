package liqp

/**
 * Creates a subList that adjusts from and to to boundaries of the list.
 */
fun <T> List<T>.safeSubList(from: Int, to: Int): List<T> {
  val size = this.size
  val start = from.coerceIn(0, size)
  val end = to.coerceIn(start, size)
  return this.subList(start, end)
}

fun Collection<*>.asSingle(): Any? {
  return when (this.size) {
    1 -> this.iterator().next()
    else -> this
  }
}
