package liqp

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

val mapper by lazy { ObjectMapper() }

@Suppress("UNCHECKED_CAST")
fun String.parseJSON(): Map<String, Any?> {
  return mapper.readValue(this, Map::class.java) as Map<String, Any?>
}

fun File.child(path: String): File = File(this, path)
