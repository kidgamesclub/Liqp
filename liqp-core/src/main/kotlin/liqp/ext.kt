package liqp

import liqp.context.LContext
import liqp.lookup.Indexable
import java.io.File
import java.util.*

fun List<Indexable>.resolve(input: Any?, context: LContext): Any? = let { indexes ->
  var value = input
  for (index in indexes) {
    value = index[value, context]
  }
  return value
}

fun File.child(path: String): File = File(this, path)

fun String.capitalizeFirstLetter():String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
fun String.decapitalizeFirstLetter():String = replaceFirstChar { it.lowercase(Locale.getDefault()) }