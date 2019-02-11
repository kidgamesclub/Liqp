package liqp

import com.google.common.base.CaseFormat.LOWER_UNDERSCORE
import com.google.common.base.CaseFormat.UPPER_CAMEL
import liqp.filter.LFilter
import liqp.tag.LTag
import java.math.BigInteger

val SnakeCaseConverter = UPPER_CAMEL.converterTo(LOWER_UNDERSCORE)
fun String.toSnakeCase() = SnakeCaseConverter.convert(this)!!

fun LTag.toSnakeCase(): String {
  return this::class.simpleName!!.replace("Tag$", "").toSnakeCase()
}

fun LFilter.toSnakeCase(): String {
  return this::class.simpleName!!.replace("Tag$", "").toSnakeCase()
}

inline fun <reified T : Any> swallow(block: () -> T): T? {
  return try {
    block()
  } catch (e: Exception) {
    // Log this???
    null
  }
}

fun Any?.isIntegral(): Boolean {
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
