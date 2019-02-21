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
    is CharSequence -> value.isBlank()
    is Char-> value.isWhitespace()
    is Array<*> -> value.asIterable().removeFalsy().isEmpty()
    is Iterable<*> -> value.removeFalsy().isEmpty()
    is Map<*, *> -> value.isEmpty()
    is Enumeration<*> -> !value.removeFalsy().isEmpty()
    is Matcher -> !value.find()
    is Nothing -> true
    is Number -> value == 0
    else -> false
  }
}

fun Iterable<*>.isEmpty() = !iterator().hasNext()
fun Enumeration<*>.isEmpty() = !iterator().hasNext()

fun Iterable<*>.removeFalsy():Iterable<*> {
  return filter {
    it.isTruthy()
  }
}

fun Enumeration<*>.removeFalsy():Iterable<*> {
  return iterator().asSequence().filter {
    it.isTruthy()
  }.asIterable()
}

