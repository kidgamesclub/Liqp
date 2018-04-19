package liqp

import java.io.File

fun TemplateFactory.assertThat(): TemplateFactoryAssert = TemplateFactoryAssert(this)

fun assertTemplateFactory(): TemplateFactoryAssert = TemplateFactory.newInstance().assertThat()

@JvmOverloads
fun assertStringTemplate(templateString: String, data: Any? = null,
                         renderer: EngineConfigurer = { this },
                         factory: FactoryConfigurer = { this }): TemplateRenderAssert {
  return createTemplateAssert({ parse(templateString) }, data, factory, renderer)
}

fun assertFileTemplate(templateFile: File, data: Any? = null,
                       renderer: EngineConfigurer = { this },
                       factory: FactoryConfigurer = { this }): TemplateRenderAssert {

  return createTemplateAssert({ parseFile(templateFile) }, data, factory, renderer)
}

@JvmOverloads
private fun createTemplateAssert(createTestTemplate: CreateTestTemplate, data: Any? = null,
                                 configureFactory: TemplateFactorySettings.() -> TemplateFactorySettings = { this },
                                 configureRenderSettings: RenderSettings.() -> Any? = { this }): TemplateRenderAssert {
  val template: Template
  try {
    val factory = TemplateFactory().toBuilder().configureFactory().build()
    template = factory.createTestTemplate()
  } catch (e: Exception) {
    return TemplateRenderAssert(error = e)
  }

  return renderAssert(template, TemplateEngine.newInstance(configureRenderSettings), data)
}

fun String.assertAsTemplate(data: Any? = null,
                            configureRenderSettings: EngineConfigurer = { this },
                            configureFactory: FactoryConfigurer = { this }): TemplateRenderAssert {
  return assertStringTemplate(this, data, configureRenderSettings, configureFactory)
}

typealias FactoryConfigurer = TemplateFactorySettings.() -> TemplateFactorySettings
typealias EngineConfigurer = RenderSettings.() -> RenderSettings
typealias CreateTestTemplate = TemplateFactory.() -> Template

fun Template.rendering(data: Any? = null, renderer: RenderSettings.()->Unit = {}): TemplateRenderAssert {
  return renderAssert(this, TemplateEngine.newInstance(renderer), data)
}

fun renderAssert(template: Template, engine: TemplateEngine, data:Any? = null) :TemplateRenderAssert{
  return try {
    val results = engine.execute(template, data)
    TemplateRenderAssert(template, results)
  } catch (e: Exception) {
    TemplateRenderAssert(template = template, error = e)
  }
}
