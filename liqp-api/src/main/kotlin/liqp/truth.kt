package liqp

import liqp.LogicResult.FALSE
import liqp.LogicResult.TRUE
import java.util.*
import java.util.regex.Matcher

fun Any?.isTruthy(): Boolean {
  return !this.isFalsy()
}

fun Any?.isFalsy(): Boolean {
  val value: Any = this ?: return true

  return when (value) {
    is Boolean -> !value
    is CharSequence -> value.isEmpty()
    is Array<*> -> value.isEmpty()
    is Collection<*> -> value.isEmpty()
    is Map<*, *> -> value.isEmpty()
    is Enumeration<*> -> !value.hasMoreElements()
    is Matcher -> !value.find()
    is Nothing -> true
    is Number -> value == 0
    else -> false
  }
}

