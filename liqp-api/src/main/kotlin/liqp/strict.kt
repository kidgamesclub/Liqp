package liqp

import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.GREATER
import liqp.ComparisonResult.LESS
import liqp.ComparisonResult.NOOP
import liqp.ComparisonResult.NULL
import liqp.exceptions.LiquidRenderingException
import kotlin.math.absoluteValue

val strictLogic = object : LLogic {

  override fun isTrue(t: Any?): Boolean {
    return when (t) {
      is LogicResult -> t == LogicResult.TRUE
      else -> !isFalse(t)
    }
  }

  override fun isFalse(t: Any?): Boolean {
    val value: Any = t ?: return true
    return when (value) {
      is Boolean -> !value
      is LogicResult -> value == LogicResult.FALSE
      else -> false
    }
  }

  override fun compareTo(a: Any?, b: Any?): ComparisonResult {
    return when {
      a === b -> EQUAL
      a == null || b == null -> NULL
      a is Number && b is Number -> a.compareNumbers(b)
      a == ControlResult.EMPTY -> size(b).compareNumbers(0)
      b == ControlResult.EMPTY -> size(a).compareNumbers(0)
      a is CharSequence && b is CharSequence -> a.toString().compareTo(b.toString()).toComparisonResult()
      isIterable(a) && isIterable(b) -> size(a).compareNumbers(size(b)).and { a == b }
      a is Map<*, *> && b is Map<*, *> -> a.size.compareNumbers(b.size).and { a == b }
      // If same type
      a is Comparable<*> && a::class.isInstance(b)-> (a as Comparable<Any>).compareTo(b).toComparisonResult()
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

  override fun add(a: Any?, b: Any?): Any? {
    //Numbers
    val aLong = asLong(a)
    val bLong = asLong(b)
    if (aLong != null && bLong != null) {
      return aLong + bLong
    }

    val aDbl = asDouble(a)
    val bDbl = asDouble(b)
    if (aDbl != null && bDbl != null) {
      return aDbl + bDbl
    }

    if (isIterable(a)) {
      val iter = asIterable(a)
      return if (isIterable(b)) {
        iter + asIterable(b)
      } else {
        iter + b
      }
    }

    return NOOP
  }

  override fun min(a: Any?, b: Any?): Number? {
    val numA = asNumber(a)
    val numB = asNumber(b)
    if (numA == null || numB == null) {
      return null
    }

    return if (numA.toDouble() <= numB.toDouble()) numA else numB
  }

  override fun div(a: Any?, b: Any?): Any? {
    //Numbers
    val aDbl = asDouble(a)
    val bDbl = asDouble(b)
    if (aDbl != null && bDbl != null) {
      if (bDbl == 0.0) {
        throw LiquidRenderingException("Div by 0")
      }
      val result = aDbl / bDbl
      return if (a.isIntegral() && b.isIntegral()) {
        result.toLong()
      } else {
        result
      }
    }
    return null
  }

  override fun mult(a: Any?, b: Any?): Any? {
    //Numbers
    val aLong = asLong(a)
    val bLong = asLong(b)
    if (aLong != null && bLong != null) {
      return aLong * bLong
    }

    val aDbl = asDouble(a)
    val bDbl = asDouble(b)
    if (aDbl != null && bDbl != null) {
      return aDbl * bDbl
    }

    return null
  }

  override fun max(a: Any?, b: Any?): Number? {

    val numA = asNumber(a)
    val numB = asNumber(b)
    if (numA == null || numB == null) {
      return null
    }

    return if (numA.toDouble() >= numB.toDouble()) numA else numB
  }

  override fun subtract(a: Any?, b: Any?): Any? {
    //Numbers
    val aLong = asLong(a)
    val bLong = asLong(b)
    if (aLong != null && bLong != null) {
      return aLong - bLong
    }

    val aDbl = asDouble(a)
    val bDbl = asDouble(b)
    if (aDbl != null && bDbl != null) {
      return aDbl - bDbl
    }

    if (isIterable(a)) {
      val iter = asIterable(a)
      return if (isIterable(b)) {
        iter - asIterable(b)
      } else {
        iter - b
      }
    }

    return NOOP
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

  override fun asLong(t: Any?): Long? {
    return when (t) {
      null -> 0
      is Double-> if(t.isIntegral()) t.toLong() else null
      is Long -> t
      is Int -> t.toLong()
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

  override fun asString(t: Any?): String? {
    return when {
      t == null -> null
      t is ControlResult-> null
      t is String -> t
      isIterable(t) -> {
        val builder = StringBuilder()
        asIterable(t)
            .filter { it !is ControlResult }
            .map { asString(it) }
            .forEach { r -> builder.append(r) }
        builder.toString()
      }
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
      is Long->t
      is Double->t
      is Short->t.toLong()
      is Int-> t.toInt()
      is Float-> t.toDouble()
      is Byte-> t.toLong()
      is Char-> t.toLong()
      is Number -> if(t.isIntegral()) t.toLong() else t.toDouble()
      is Boolean -> if (t) 1L else 0L
      is CharSequence -> t.toString().toNumberOrNull()
      else -> null
    }
  }

  override fun isEmpty(t: Any?): Boolean {
    return size(t) == 0
  }

  override fun size(t: Any?): Int {
    return when (t) {
      null -> 0
      is Boolean -> if (t) 1 else 0
      is Long-> {
        val len = t.toString().length / 2
        len + len%2
      }
      is Collection<*> -> t.size
      is Array<*> -> t.size
      is CharSequence -> t.trim().length
      is Map<*, *> -> t.size
      is Iterable<*> -> t.count()
      else -> 0
    }
  }
}

private fun Number.compareNumbers(num: Number): ComparisonResult {
  val delta = this.toDouble() - num.toDouble()
  return when {
    delta.absoluteValue < 0.00000000001 -> EQUAL
    delta < 0 -> LESS
    delta > 0 -> GREATER
    else -> NOOP
  }
}

private fun Boolean.asComparison(): ComparisonResult {
  return if (this) EQUAL else NOOP
}
