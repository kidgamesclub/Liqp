package liqp.parser

enum class Flavor constructor(val includesDirName: String) {
  LIQUID("snippets"),
  JEKYLL("_includes")
}
