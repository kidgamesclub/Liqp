package liqp

import com.fasterxml.jackson.databind.ObjectMapper

val mapper = ObjectMapper()

fun String.parseJSON(): Map<String, Any?> {
  return mapper.readValue(this, Map::class.java) as Map<String, Any?>
}

fun Any?.toNonNullString(): String {
  return when (this == null) {
    true -> ""
    false -> this.toString()
  }
}

//fun String.capitalize():String {
//  return when{
//    this.isEmpty() -> this
//    else-> Character.toUpperCase(this.charAt(0)) + this.substring(1);
//  }
//}
//
//
