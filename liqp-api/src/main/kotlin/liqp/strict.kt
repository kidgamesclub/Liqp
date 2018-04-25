package liqp

import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.GREATER
import liqp.ComparisonResult.LESS
import liqp.ComparisonResult.NOOP
import liqp.ComparisonResult.NULL
import java.util.*
import kotlin.math.absoluteValue

val strictLogic = object : LLogic {

  override fun isTrue(t: Any?): Boolean {
    TODO()
  }

  override fun isFalse(t: Any?): Boolean {
    TODO()
  }

  override fun compareTo(a: Any?, b: Any?): ComparisonResult {
    return when {
      a === b -> EQUAL
      a == null -> if (b == null) NULL else LESS
      a is Number && b is Number -> a.compareNumbers(b)
      a is CharSequence && b is CharSequence -> a.length.compareNumbers(b.length).and { a == b }
      isIterable(a) && isIterable(b) -> size(a).compareNumbers(size(b)).and { a == b }
      a is Map<*, *> && b is Map<*, *> -> a.size.compareNumbers(b.size).and { a == b }
      else -> (a == b).asComparison()
    }
  }

  override fun areEqual(a: Any?, b: Any?): LogicResult {
    return when (compareTo(a, b)) {
      EQUAL -> LogicResult.TRUE
      NOOP -> LogicResult.NOOP
      else -> LogicResult.FALSE
    }
  }

  override fun add(t1: Any?, t2: Any?): Any? {
    TODO()
  }

  override fun subtract(t1: Any?, t2: Any?): Any? {
    TODO()
  }

  override fun range(from: Any?, to: Any?): Any? {
    TODO()
  }

  override fun contains(t: Any?): LogicResult {
    TODO()
  }

  override fun isIterable(t: Any?): Boolean {
    return t is Iterable<*> || t is Array<*>
  }

  override fun asIntegral(t: Any?): Long? {
    return when (t) {
      null -> 0
      is Long -> t
      is Int -> t.toLong()
      is Boolean -> if (t) 1 else 0
      is String -> t.toLongOrNull()
      else -> null
    }
  }

  override fun asIterable(t: Any?): Iterable<Any> {
    return when (t) {
      null -> emptyList()
      is Iterable<*> -> t.filterNotNull()
      is Array<*> -> t.filterNotNull()
      is Map<*, *> -> t.asIterable()
      else -> listOf(t)
    }
  }

  override fun asString(t: Any?): String {
    return when (t) {
      is String -> t
      is Array<*> -> Arrays.toString(t)
      else -> t.toString()
    }
  }

  override fun asDouble(t: Any?): Double? {
    return when (t) {
      null -> 0.0
      is Number -> t.toDouble()
      is Boolean -> if (t) 1.0 else 0.0
      is String -> t.toDoubleOrNull()
      else -> null
    }
  }

  override fun asNumber(t: Any?): Number? {
    return when (t) {
      null -> 0.0
      is Number -> t
      is Boolean -> if (t) 1 else 0
      is String -> t.toDoubleOrNull()
      else -> null
    }
  }

  override fun isEmpty(t: Any?): Boolean {
    return size(t) == 0
  }

  override fun size(t: Any?): Int {
    return when (t) {
      null -> 0
      is Collection<*> -> t.size
      is Array<*> -> t.size
      is CharSequence -> t.trim().length
      is Map<*, *> -> t.size
      is Iterable<*> -> t.count()
      else -> 0
    }
  }
}

private inline fun Number.compareNumbers(num: Number): ComparisonResult {
  val delta = this.toDouble() - num.toDouble()
  return when {
    delta.absoluteValue < 0.00000000001 -> EQUAL
    delta < 0 -> LESS
    delta > 0 -> GREATER
    else -> NOOP
  }
}

private inline fun Boolean.asComparison(): ComparisonResult {
  return if (this) EQUAL else GREATER
}
