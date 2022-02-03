package liqp


import liqp.filter.LFilter
import liqp.tag.LTag
import java.io.StringReader
import java.math.BigInteger
import javax.json.Json



fun String.parseJSON(): Any? {
  val reader = Json.createReader(StringReader(this))
  val parsed = reader.read()
  return parsed.unboxAsAny()
}

fun LTag.toSnakeCase(): String {
  return this::class.simpleName!!.replace("Tag$", "").toSnakeCase()
}

fun LFilter.toSnakeCase(): String {
  return this::class.simpleName!!.replace("Filter$", "").toSnakeCase()
}

inline fun <reified T : Any> swallow(block: () -> T): T? {
  return try {
    block()
  } catch (e: Exception) {
    // Log this???
    null
  }
}

fun Any?.isIntegralType(): Boolean {
  return when (this) {
    is Long -> true
    is Int -> true
    is BigInteger -> true
    else -> false
  }
}

fun <T : Any> Iterator<T>.find(filter: (T) -> Boolean): T {
  while (this.hasNext()) {
    val n = this.next()
    if (filter(n)) {
      return n
    }
  }

  throw NoSuchElementException()
}

fun String.toSnakeCase():String {


  // Empty String
  // Empty String
  var result = ""

  // Append first character(in lower case)
  // to result string

  // Append first character(in lower case)
  // to result string
  val c: Char = this[0]
  result += c.lowercaseChar()

  // Traverse the string from
  // ist index to last index

  // Traverse the string from
  // ist index to last index
  for (i in 1 until this.length) {
    val ch: Char = this[i]

    // Check if the character is upper case
    // then append '_' and such character
    // (in lower case) to result string
    if (Character.isUpperCase(ch)) {
      result = result + '_'
      result = (result
              + ch.lowercaseChar())
    } else {
      result = result + ch
    }
  }

  // return the result

  // return the result
  return result
}