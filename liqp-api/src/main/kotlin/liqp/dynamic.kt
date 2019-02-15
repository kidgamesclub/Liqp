package liqp

import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.GREATER
import liqp.ComparisonResult.LESS

interface InstanceFacts {
  fun isIterable(t: Any?): Boolean
  fun isEmpty(t: Any?): Boolean
  fun asLong(t: Any?): Long?
  fun asInteger(t: Any?): Int? = asLong(t)?.toInt()
  fun isIntegral(t: Any?): Boolean = asLong(t) != null
  fun asDouble(t: Any?): Double?
  fun asIterable(t: Any?): Iterable<Any?>
  fun asString(t: Any?): String?
  fun asNumber(t: Any?): Number?
  fun size(t: Any?): Int
}

class TypeCoersion(private val facts: InstanceFacts,
                   private val truth: Truth) : InstanceFacts by facts, Truth by truth {

  inline fun <reified T : Any> coerceOrNull(from: Any?): T? {
    return coerceOrNull(from, T::class.java)
  }

  @Suppress("unchecked_cast")
  fun <T : Any> coerceOrNull(from: Any?, to: Class<T>): T? {
    val value = from ?: return null
    return when (to) {
      List::class.java -> asIterable(value).toList() as T?
      String::class.java, java.lang.String::class.java -> asString(value) as T?
      Boolean::class.java, java.lang.Boolean::class.java -> isTrue(value) as T?
      Int::class.java -> asInteger(value) as T?
      Integer::class.java, java.lang.Integer::class.java -> asInteger(value) as T?
      Long::class.java, java.lang.Long::class.java -> asLong(value) as T?
      Double::class.java, java.lang.Double::class.java -> asDouble(value) as T?
      else -> {
        when {
          Number::class.java.isAssignableFrom(to) -> asNumber(value) as T?
          Iterable::class.java.isAssignableFrom(to) -> asIterable(value) as T?
          else -> value as T?
        }
      }
    }
  }
}

interface Truth {
  fun isTrue(t: Any?): Boolean
  fun isFalse(t: Any?): Boolean
}

interface Comparisons {
  fun compareTo(a: Any?, b: Any?): ComparisonResult
  fun areEqual(a: Any?, b: Any?): LogicResult
}

interface Combiners {
  fun add(a: Any?, b: Any?): Any?
  fun div(a: Any?, b: Any?): Any?
  fun mult(a: Any?, b: Any?): Any?
  fun subtract(a: Any?, b: Any?): Any?
  fun min(a: Any?, b: Any?): Number?
  fun max(a: Any?, b: Any?): Number?
}

enum class ComparisonResult {
  NULL,
  NOOP,
  GREATER,
  LESS,
  EQUAL;

  fun also(check: () -> ComparisonResult): ComparisonResult {
    return when (this) {
      EQUAL -> check()
      else -> this
    }
  }

  fun and(check: () -> Boolean): ComparisonResult {
    return when (this) {
      EQUAL -> if (check()) EQUAL else GREATER
      else -> this
    }
  }

  fun toInt(): Int {
    return when (this) {
      GREATER -> 1
      EQUAL -> 0
      else -> -1
    }
  }
}

enum class LogicResult {
  NOOP,
  TRUE,
  FALSE;

  val str = name.toLowerCase()
  override fun toString() = str
}

fun Int.toComparisonResult(): ComparisonResult {
  return when (this) {
    0 -> EQUAL
    else -> {
      if (this < 0) LESS else GREATER
    }
  }
}

interface LLogic : InstanceFacts, Truth, Comparisons, Combiners
