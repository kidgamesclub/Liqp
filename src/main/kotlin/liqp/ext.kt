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

//inline fun Any?.isTruthy() {
//
//}
//
//fun Any?.isFalsy():Boolean {
//  val value: Any = this ?: return true
//
//  when (value) {
//    is Boolean-> !value
//    is CharSequence-> value.isEmpty()
//    is Array<*>-> value.isEmpty()
//    is Collection<*>-> value.isEmpty()
//    is Map<*,*>-> value.isEmpty()
//    is
//  }
//  if (value is Boolean && !(value as Boolean))
//    return true
//
//  if (value is CharSequence && (value as CharSequence).length == 0)
//    return true
//
//  if (this.isArray(value) && this.asArray(value).length === 0)
//    return true
//
//  return if (value is Map<*, *> && (value as Map<*, *>).isEmpty()) true else false
//}

