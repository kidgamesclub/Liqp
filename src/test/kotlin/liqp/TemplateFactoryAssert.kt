package liqp

import java.io.File

class TemplateFactoryAssert(val factory: TemplateFactory, val engine: TemplateEngine = TemplateEngine.newInstance()) {

  fun withSettings(configurer: TemplateFactorySettings.() -> TemplateFactorySettings): TemplateFactoryAssert {
    return factory.toBuilder().configurer().build().assertThat()
  }

  fun withTemplateString(template: String): TemplateAssert {
    try {
      return TemplateAssert(factory.parse(template), engine)
    } catch (e: Exception) {
      return TemplateAssert(error = e)
    }
  }

  fun withTemplateFile(template: File): TemplateAssert {
    try {
      return TemplateAssert(factory.parseFile(template), engine)
    } catch (e: Exception) {
      return TemplateAssert(error = e)
    }
  }
}
