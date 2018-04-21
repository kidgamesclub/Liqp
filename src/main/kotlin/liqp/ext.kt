package liqp

import com.fasterxml.jackson.databind.ObjectMapper

val mapper = ObjectMapper()

@Suppress("UNCHECKED_CAST")
fun String.parseJSON(): Map<String, Any?> {
  return mapper.readValue(this, Map::class.java) as Map<String, Any?>
}

fun Any?.toNonNullString(): String {
  return when (this == null) {
    true -> ""
    false -> this.toString()
  }
}
