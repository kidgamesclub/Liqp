package liqp


fun String.truncateEllipses(maxLength: Int): String {
  return when {
    maxLength == 0 -> ""
    this.length > maxLength -> truncate(maxLength - 1) + '…'
    else -> this
  }
}

fun String.truncate(maxLength: Int): String = substring(0, length.coerceIn(0..kotlin.math.max(0, maxLength)))
fun String.uncapitalize() = replaceFirstChar{ it.uppercaseChar() }
fun ByteArray.toHex() = joinToString("") { "%02X".format((it.toInt() and 0xFF)) }.lowercase()
fun String.stripWhitespace(): String {
  return this.replace(Regex("\\s"), "")
}

fun String.splitting(vararg on: String, ignoreCase: Boolean = false, limit: Int = 0, removeBlank: Boolean = true): List<String> = this.split(delimiters = on,
    ignoreCase = ignoreCase, limit = limit)
    .filter { !removeBlank || it.isNotBlank() }
    .map(String::trim)

fun String.splitting(vararg on: Char, ignoreCase: Boolean = false, limit: Int = 0, removeBlank: Boolean = true): List<String> = this.split(delimiters = on,
    ignoreCase = ignoreCase, limit = limit)
    .filter { !removeBlank || it.isNotBlank() }
    .map(String::trim)



fun String.indent(indent: String = "\t", replaceFirst: Boolean = true) =
    this.prependIndent(indent).let {
      when (replaceFirst) {
        true -> it
        else -> it.trimStart('\t')
      }
    }

fun Iterable<String>.joinIndexed(separator: String = "\n",
                                 transform: (Int, String) -> String = { _, s -> s }): String {
  var count = 0
  return joinToString(separator) { line ->
    transform(count++, line)
  }
}

fun <A> Iterable<A>.join(separator: String = "\n",
                         start: String = "",
                         end: String = "", transform: (A) -> String = { it.toString() }): String {
  val builder = StringBuilder()
  if (this.any()) builder.append(start)
  joinTo(builder, separator, transform = transform)
  if (this.any()) builder.append(end)
  return builder.toString()
}

fun Iterable<Any?>.indent(indent: String = "\t", replaceFirst: Boolean = true,
                          transform: (String) -> String = { it }) =
    this.joinToString("\n") { transform(it.toString().prependIndent(indent)) }.run {
      when (replaceFirst) {
        true -> this
        else -> this.trimStart('\t')
      }
    }

operator fun StringBuilder.plusAssign(append: Any?) {
  this.append(append)
}

operator fun StringBuilder.plus(append: Any?): StringBuilder {
  return this.append(append)
}

infix fun CharSequence?.then(append: CharSequence): String = when {
  this == null -> ""
  this.isBlank() -> ""
  else -> "$this$append"
}

infix fun CharSequence?.isIn(strings: Iterable<String?>): Boolean = let { searchTerm ->
  val terms = "$searchTerm".splitting(' ').asSequence()
  strings.asSequence()
      .filterNotNull()
      .flatMap { value ->
        value.splitToSequence(' ')
      }
      .distinct()
      .flatMap { token ->
        terms.map { term ->
          token to term
        }
      }
      .any {
        it.first.contains(it.second, ignoreCase = true)
      }
}

fun CharSequence?.findMatches(strings: Iterable<String?>, ignoreCase: Boolean = true): Sequence<TermSearchResult<String>> = let { term ->
  term.findMatches(strings.filterNotNull(), ignoreCase) { listOf(this) }
}

fun <A : Any> CharSequence?.findMatches(items: Iterable<A>, ignoreCase: Boolean = true, stringify: A.() -> Iterable<Any?>): Sequence<TermSearchResult<A>> = let { searchTerm ->
  val terms = "$searchTerm".splitting(' ')
  val pairs = items.map { it to it.stringify().mapNotNull { token -> token?.toString() } }
  pairs.asSequence()
      .distinct()
      .map { (item, tokens) ->
        val checks = tokens.flatMap { token -> terms.map { term -> term to token } }
        val matches = checks.filter { (term, token) ->
          token.contains(term, ignoreCase = ignoreCase)
        }
        val termMatches = matches.map { it.first }.toSet()
        val tokenMatches = matches.map { it.second }.toSet()
        return@map TermSearchResult(item, termMatches, tokenMatches, matchAll = termMatches.size >= terms.size)
      }
      .filter { result -> result.isMatch }
}

data class TermSearchResult<A : Any>(val result: A, val matchedTerms: Set<String>, val matchedTokens: Set<String>, val matchAll: Boolean) : Comparable<TermSearchResult<*>> {
  override fun compareTo(other: TermSearchResult<*>): Int =
      other.matchAll.compareTo(matchAll).takeUnless { it == 0 }
          ?: other.matchedTerms.size.compareTo(matchedTerms.size).takeUnless { it == 0 }
          ?: other.matchedTokens.size.compareTo(matchedTokens.size).takeUnless { it == 0 }
          ?: "$result".compareTo("${other.result}")

  val isMatch = matchedTerms.isNotEmpty()
}

infix fun Iterable<String?>.hasTerm(term: CharSequence): Boolean = term isIn this



