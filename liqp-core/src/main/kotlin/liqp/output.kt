package liqp

fun Any?.toNonNullString(): String {
  return when (this == null) {
    true -> ""
    false -> this.toString()
  }
}

val Any.hasContent
  get() = this !is String || this.isNotBlank()

