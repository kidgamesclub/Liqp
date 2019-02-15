package liqp

import liqp.ComparisonResult.EQUAL
import liqp.ComparisonResult.GREATER
import liqp.ComparisonResult.LESS
import liqp.ComparisonResult.NOOP
import liqp.ComparisonResult.NULL
import liqp.coersion.toIterable
import liqp.coersion.toNumber
import liqp.exceptions.LiquidRenderingException
import kotlin.math.absoluteValue

val strictLogic = StrictLogic()

class StrictLogic : LLogic {
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
      a is Comparable<*> && a::class.isInstance(b) -> (a as Comparable<Any>).compareTo(b).toComparisonResult()
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
      return if (a.isIntegralType() && b.isIntegralType()) {
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

  override fun isIterable(t: Any?): Boolean {
    return t is Iterable<*> || t is Array<*>
  }

  override fun asLong(t: Any?): Long? {
    val number = asNumber(t) ?: return null
    return when (number) {
      is Double -> if (t.isIntegralType()) number.toLong() else null
      is Float -> if (t.isIntegralType()) number.toLong() else null
      is Long -> number
      is Int -> number.toLong()
      is Short -> number.toLong()
      else -> null
    }
  }

  override fun asIterable(t: Any?): Iterable<Any?> {
    return toIterable.coerce(t)
  }

  override fun asString(t: Any?): String? {
    return when {
      t == null -> null
      t is ControlResult -> null
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

  override fun asDouble(t: Any?): Double? = asNumber(t)?.toDouble()

  override fun asNumber(t: Any?): Number? {
    val number = toNumber.coerceOrNull(t)
    return when (number) {
      is Long -> number
      is Double -> number
      is Short -> number.toLong()
      is Int -> number.toInt()
      is Float -> number.toDouble()
      is Byte -> number.toLong()
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
      is Long -> {
        val len = t.toString().length / 2
        len + len % 2
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
