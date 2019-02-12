package liqp.coersion

import lang.coersion.ConversionStrategy
import lang.coersion.Converter
import lang.json.JsrArray
import lang.json.JsrNumber
import lang.json.JsrObject
import lang.json.JsrValue
import lang.json.number
import lang.json.unbox
import lang.json.unboxAsAny
import liqp.toNumberOrNull
import kotlin.math.roundToInt

object ToNumberOrNull : Converter<Number?>() {
  override val strategy: ConversionStrategy<Number?> = { input, fallback ->

    when (input) {
      null -> null
      is JsrNumber-> input.numberValue()
      is Number -> input
      is Char -> input.toLong()
      is Byte -> input.toLong()
      is CharSequence -> input.toString().toNumberOrNull()
      is Boolean -> null
      else -> fallback(input)
    }
  }
}

object Unboxer : Converter<Any?>() {
  override val strategy: ConversionStrategy<Any?> = { input, _ ->
    when (input) {
      null -> null
      is JsrValue -> input.unboxAsAny()
      else -> input
    }
  }
}

object ToIterable : Converter<Iterable<*>>() {
  override val strategy: ConversionStrategy<Iterable<*>> = { input, _ ->
    when (input) {
      null -> emptyList<Any>()
      is JsrArray -> input.unbox()
      is JsrObject -> input.unbox().map { (k, v) -> k to v }
      is Array<*> -> listOf(*input)
      is Iterable<*> -> input
      is Map<*, *> -> input.asIterable()
      else -> listOf(input)
    }
  }
}


