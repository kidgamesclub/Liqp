package liqp

import assertk.Assert
import assertk.fail
import liqp.config.MutableParseSettings
import liqp.config.MutableRenderSettings
import liqp.filter.LFilter
import liqp.node.LTemplate
import java.io.File

typealias ParseConfigurer = MutableParseSettings.() -> MutableParseSettings
typealias RenderConfigurer = MutableRenderSettings.() -> MutableRenderSettings
typealias CreateTestTemplate = LParser.() -> LTemplate

fun LiquidParser.assertThat(): LiquidParserAssert = LiquidParserAssert(this)
fun assertParser(): LiquidParserAssert = LiquidParser.newInstance().assertThat()

/**
 * Creates a {@link TemplateRenderAssert} instance for a string template, and allows overloaded
 * configurers for the renderer and/or parser
 */
@JvmOverloads
fun assertStringTemplate(templateString: String,
                         data: Any? = null,
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
  val template: LTemplate
  try {
    val parser = MutableParseSettings(defaultParseSettings).configureParser().toParser()
    template = parser.createTestTemplate()
  } catch (e: Exception) {
    return TemplateRenderAssert(error = e)
  }

  return executeTemplateAndAssert(template, LiquidRenderer.newInstance(configureRenderer), data)
}

fun executeTemplateAndAssert(template: LTemplate, engine: LRenderer, data: Any? = null): TemplateRenderAssert {
  return try {
    val context = engine.createRenderContext(data)
    val results = engine.executeWithContext(template, context)


    TemplateRenderAssert(template = template,
        renderResult = results,
        context=context)
  } catch (e: Exception) {
    TemplateRenderAssert(template = template, error = e)
  }
}

fun renderTemplateAndAssert(template: LTemplate, engine: LRenderer, data: Any? = null): TemplateRenderAssert {
  return try {
    val context = engine.createRenderContext(data)
    val results = engine.renderWithContext(template, context)

    TemplateRenderAssert(template = template,
        renderResult = results,
        context=context)
  } catch (e: Exception) {
    TemplateRenderAssert(template = template, error = e)
  }
}

fun LTemplate.executing(data: Any? = null, renderer: MutableRenderSettings.() -> Unit = {}): TemplateRenderAssert {
  return executeTemplateAndAssert(this, LiquidRenderer.newInstance(renderer), data)
}

fun LTemplate.rendering(data: Any? = null, renderer: MutableRenderSettings.() -> Unit = {}): TemplateRenderAssert {
  return renderTemplateAndAssert(this, LiquidRenderer.newInstance(renderer), data)
}

fun LTemplate.assertThat(): TemplateAssert {
  return TemplateAssert(this, this.renderer)
}


fun LFilter.assertThat(): FilterAssert {
  return FilterAssert(this)
}

inline fun <reified T:Any> T?.asserting(): Assert<T> {
  return when (this) {
    null-> {
      fail("Expected non-null value for ${T::class.qualifiedName}")
      TODO()
    }
    else-> assertk.assert(this)
  }
}

fun <T:Any> assertThat(subject:T?): Assert<T> {
  return when (subject) {
    null-> {
      fail("Expected non-null value")
      TODO()
    }
    else-> assertk.assert(subject)
  }
}


inline fun <reified T:Any> T?.assertNullable(): Assert<T?> {
  return assertk.assert(this)
}

