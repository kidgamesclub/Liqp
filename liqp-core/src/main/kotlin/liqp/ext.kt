package liqp

import com.fasterxml.jackson.databind.ObjectMapper

val mapper by lazy { ObjectMapper() }

@Suppress("UNCHECKED_CAST")
fun String.parseJSON(): Map<String, Any?> {
  return mapper.readValue(this, Map::class.java) as Map<String, Any?>
}
