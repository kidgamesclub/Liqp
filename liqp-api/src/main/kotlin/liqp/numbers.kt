package liqp

fun String.asNumber():Number {
  return toNumberOrNull() ?: throw NumberFormatException("Invalid number: '${this}'")
}

fun String.toNumberOrNull():Number? {
  return this.toLongOrNull() ?: this.toDoubleOrNull()
}
