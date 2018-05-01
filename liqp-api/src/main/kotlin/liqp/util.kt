package liqp

import com.google.common.base.CaseFormat
import liqp.filter.LFilter
import liqp.tag.LTag
import java.math.BigInteger

val SNAKE_CONVERTER = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE)

fun LTag.toSnakeCase(): String {
  return SNAKE_CONVERTER.convert(this::class.java.simpleName.replace("Tag$", ""))!!
}

fun LFilter.toSnakeCase(): String {
  return SNAKE_CONVERTER.convert(this::class.java.simpleName.replace("Tag$", ""))!!
}

inline fun <reified T : Any> swallow(block: () -> T): T? {
  return try {
    block()
  } catch (e: Exception) {
    // Log this???
    null
  }
}

fun Long.neededBits(): Int {
  var value = this
  var count = 0
  while (value > 0) {
    count++
    value = value shr 1
  }
  return count
}

fun Number.toNumber(): Number {
  return when (this.isIntegral()) {
    true -> this.toLong()
    else -> this.toDouble()
  }
}

fun Any?.isIntegral():Boolean {
  return when (this) {
    is Long -> true
    is Int -> true
    is BigInteger -> true
    else-> false
  }
}

fun Any?.canBeIntegral():Boolean {
  return when (this) {
    is Long -> true
    is Int -> true
    is BigInteger -> true
    is Number-> toDouble() % 1.0 == 0.0
    else -> false
  }
}

/**
 * Pulled from Stackoverflow
 */
class AlphanumComparator : Comparator<String> {
  override fun compare(s1: String, s2: String): Int {
    var thisMarker = 0
    var thatMarker = 0
    val s1Length = s1.length
    val s2Length = s2.length

    while (thisMarker < s1Length && thatMarker < s2Length) {
      val thisChunk = getChunk(s1, s1Length, thisMarker)
      thisMarker += thisChunk.length

      val thatChunk = getChunk(s2, s2Length, thatMarker)
      thatMarker += thatChunk.length

      // If both chunks contain numeric characters, sort them numerically.
      var result: Int
      if (isDigit(thisChunk[0]) && isDigit(thatChunk[0])) {
        // Simple chunk comparison by length.
        val thisChunkLength = thisChunk.length
        result = thisChunkLength - thatChunk.length
        // If equal, the first different number counts.
        if (result == 0) {
          for (i in 0..thisChunkLength - 1) {
            result = thisChunk[i] - thatChunk[i]
            if (result != 0) {
              return result
            }
          }
        }
      } else {
        result = thisChunk.compareTo(thatChunk)
      }

      if (result != 0) {
        return result
      }
    }

    return s1Length - s2Length
  }

  private fun getChunk(string: String, length: Int, marker: Int): String {
    var current = marker
    val chunk = StringBuilder()
    var c = string[current]
    chunk.append(c)
    current++
    if (isDigit(c)) {
      while (current < length) {
        c = string[current]
        if (!isDigit(c)) {
          break
        }
        chunk.append(c)
        current++
      }
    } else {
      while (current < length) {
        c = string[current]
        if (isDigit(c)) {
          break
        }
        chunk.append(c)
        current++
      }
    }
    return chunk.toString()
  }

  private fun isDigit(ch: Char): Boolean {
    return ch in '0'..'9'
  }
}

fun Iterable<String>.sortAlphaNumeric():List<String> = this.sortedWith(liqp.AlphanumComparator())

fun <T:Any> Iterator<T>.find(filter:(T)->Boolean):T {
  while (this.hasNext()) {
    val n = this.next()
    if (filter(n)) {
      return n
    }
  }

  throw NoSuchElementException()
}
