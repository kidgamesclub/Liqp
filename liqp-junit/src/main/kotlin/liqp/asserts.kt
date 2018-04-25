package liqp

import liqp.config.MutableParseSettings
import liqp.config.MutableRenderSettings
import liqp.filter.LFilter
import java.io.File

typealias ParseConfigurer = MutableParseSettings.() -> MutableParseSettings
typealias RenderConfigurer = MutableRenderSettings.() -> MutableRenderSettings
typealias CreateTestTemplate = LiquidParser.() -> Template

fun LiquidParser.assertThat(): LiquidParserAssert = LiquidParserAssert(this)
fun assertParser(): LiquidParserAssert = LiquidParser.newInstance().assertThat()

/**
 * Creates a {@link TemplateRenderAssert} instance for a string template, and allows overloaded
 * configurers for the renderer and/or parser
 */
@JvmOverloads
fun assertStringTemplate(templateString: String, data: Any? = null,
                         renderer: RenderConfigurer = { this },
                         parser: ParseConfigurer = { this }): TemplateRenderAssert {
  return createTemplateAssert({ parse(templateString) }, data, parser, renderer)
}

fun assertFileTemplate(templateFile: File, data: Any? = null,
                       renderer: RenderConfigurer = { this },
                       parser: ParseConfigurer = { this }): TemplateRenderAssert {

  return createTemplateAssert({ parseFile(templateFile) }, data, parser, renderer)
}

private fun createTemplateAssert(createTestTemplate: CreateTestTemplate, data: Any? = null,
                                 configureParser: ParseConfigurer = { this },
                                 configureRenderer: RenderConfigurer = { this }): TemplateRenderAssert {
  val template: Template
  try {
    val factory = MutableParseSettings().configureParser().toParser()
    template = factory.createTestTemplate()
  } catch (e: Exception) {
    return TemplateRenderAssert(error = e)
  }

  return renderAssert(template, LiquidRenderer.newInstance(configureRenderer), data)
}

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
