package liqp.coersion

import lang.coersion.coersionOf
import lang.json.JsrArray
import lang.json.JsrNumber
import lang.json.JsrObject
import lang.json.unbox
import liqp.toNumberOrNull

val toNumber = coersionOf<Number> { input, _ ->
  when (input) {
    null -> null
    is JsrNumber -> input.numberValue()
    is Number -> input
    is Char -> input.toLong()
    is Byte -> input.toLong()
    is CharSequence -> input.toString().toNumberOrNull()
    is Boolean -> null
    else -> null
  }
}

val toIterable = coersionOf<Iterable<*>> { input, _ ->
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



