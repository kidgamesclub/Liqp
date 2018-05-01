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
