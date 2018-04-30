package liqp

interface InstanceFacts {
  fun isIterable(t: Any?): Boolean
  fun isEmpty(t: Any?): Boolean
  fun asLong(t: Any?): Long?
  fun asInteger(t: Any?): Int? = asLong(t)?.toInt()
  fun isIntegral(t: Any?): Boolean = asLong(t) != null
  fun asDouble(t: Any?): Double?
  fun asIterable(t: Any?): Iterable<Any>
  fun asString(t: Any?): String?
  fun asNumber(t: Any?): Number?
  fun size(t:Any?):Int
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
  fun range(from:Any?, to:Any?): Any?
  fun min(a:Any?, b:Any?):Number?
  fun max(a:Any?, b:Any?):Number?
  fun contains(t:Any?):LogicResult
}

enum class ComparisonResult {
  NULL,
  NOOP,
  GREATER,
  LESS,
  EQUAL;

  fun also(check:()->ComparisonResult):ComparisonResult {
    return when (this) {
      EQUAL-> check()
      else-> this
    }
  }

  fun and(check:()->Boolean):ComparisonResult {
    return when (this) {
      EQUAL-> if(check()) EQUAL else GREATER
      else -> this
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

enum class DynamicType {
  TEXT,
  DATE,
  NUMBER,
  BOOLEAN,
  ARRAY,
  MAP
}

interface LLogic : InstanceFacts, Truth, Comparisons, Combiners

data class LiquidLogic(val types: InstanceFacts,
                       val truth: Truth,
                       val comparisons: Comparisons,
                       val combiners: Combiners): LLogic, InstanceFacts by types,
    Truth by truth, Comparisons by comparisons, Combiners by combiners

