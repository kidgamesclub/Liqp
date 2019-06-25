package liqp.context

interface LResolver {
  fun loadTemplate(path: String): String
}