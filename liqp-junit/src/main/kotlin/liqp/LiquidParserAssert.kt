package liqp

import java.io.File

class LiquidParserAssert(private val parser: LParser,
                         private val engine: LRenderer = parser.toRenderer()) {

  fun withTemplateString(template: String): TemplateAssert {
    try {
      return TemplateAssert(parser.parse(template), engine)
    } catch (e: Exception) {
      return TemplateAssert(error = e)
    }
  }

  fun withTemplateFile(template: File): TemplateAssert {
    try {
      return TemplateAssert(parser.parseFile(template), engine)
    } catch (e: Exception) {
      return TemplateAssert(error = e)
    }
  }
}
