package liqp

/**
 * Creates a substring that's safe for start and end index.
 */
fun CharSequence.safeSubstring(from: Int, to: Int): String {
  val size = this.length
  if (from < 0) {
    return ""
  }
  val start = from.coerceIn(0, size)
  val end = to.coerceIn(start-1, size)
  return this.substring(start until end)
}
