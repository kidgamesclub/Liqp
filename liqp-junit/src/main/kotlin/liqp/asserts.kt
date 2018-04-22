package liqp

import liqp.filters.LFilter
import java.io.File

fun LiquidParser.assertThat(): TemplateFactoryAssert = TemplateFactoryAssert(this)

fun assertTemplateFactory(): TemplateFactoryAssert = LiquidParser.newInstance().assertThat()

@JvmOverloads
fun assertStringTemplate(templateString: String, data: Any? = null,
                         renderer: RenderConfigurer = { this },
                         factory: ParseConfigurer = { this }): TemplateRenderAssert {
  return createTemplateAssert({ parse(templateString) }, data, factory, renderer)
}

fun assertFileTemplate(templateFile: File, data: Any? = null,
                       renderer: RenderConfigurer = { this },
                       factory: ParseConfigurer = { this }): TemplateRenderAssert {

  return createTemplateAssert({ parseFile(templateFile) }, data, factory, renderer)
}

private fun createTemplateAssert(createTestTemplate: CreateTestTemplate, data: Any? = null,
                                 configureFactory: ParseConfigurer = { this },
                                 configureRenderSettings: RenderConfigurer = { this }): TemplateRenderAssert {
  val template: Template
  try {
    val factory = MutableParseSettings().configureFactory().toParser()
    template = factory.createTestTemplate()
  } catch (e: Exception) {
    return TemplateRenderAssert(error = e)
  }

  return renderAssert(template, LiquidRenderer.newInstance(configureRenderSettings), data)
}

typealias ParseConfigurer = MutableParseSettings.() -> MutableParseSettings
typealias RenderConfigurer = MutableRenderSettings.() -> MutableRenderSettings
typealias CreateTestTemplate = LiquidParser.() -> Template


fun renderAssert(template: Template, engine: LiquidRenderer, data:Any? = null) :TemplateRenderAssert{
  return try {
    val results = engine.execute(template, data)
    TemplateRenderAssert(template, results)
  } catch (e: Exception) {
    TemplateRenderAssert(template = template, error = e)
  }
}

fun Template.rendering(data: Any? = null, renderer: MutableRenderSettings.()->Unit = {}): TemplateRenderAssert {
  return renderAssert(this, LiquidRenderer.newInstance(renderer), data)
}

fun LFilter.withAssertions(): FilterAssert {
  return FilterAssert(this)
}
