package liqp

import assertk.Assert
import assertk.fail
import liqp.config.MutableParseSettings
import liqp.config.MutableRenderSettings
import liqp.filter.LFilter
import liqp.node.LTemplate
import java.io.File
import java.time.ZoneId
import java.util.*

typealias ParseConfigurer = MutableParseSettings.() -> Unit
typealias RenderConfigurer = MutableRenderSettings.() -> Unit
typealias CreateTestTemplate = LParser.() -> LTemplate

fun LParser.assertThat(): LiquidParserAssert = LiquidParserAssert(this)
fun assertParser(): LiquidParserAssert = provider.createParser().assertThat()

/**
 * Creates a {@link TemplateRenderAssert} instance for a string template, and allows overloaded
 * configurers for the renderer and/or parser
 */
@JvmOverloads
fun assertStringTemplate(templateString: String,
                         data: Any? = null,
                         renderer: RenderConfigurer = { this },
                         parser: ParseConfigurer = { this }): TemplateRenderAssert {
  return createTemplateAssert({ parse(templateString) }, data.parseIfNecessary(), parser, renderer)
}

fun Any?.parseIfNecessary(): Any? = when (this) {
  is String -> this.parseJSON()
  else -> this
}

fun assertFileTemplate(templateFile: File, data: Any? = null,
                       renderer: RenderConfigurer = { this },
                       parser: ParseConfigurer = { this }): TemplateRenderAssert {

  return createTemplateAssert({ parseFile(templateFile) }, data, parser, renderer)
}

private fun createTemplateAssert(createTestTemplate: CreateTestTemplate, data: Any? = null,
                                 configureParser: ParseConfigurer = { },
                                 configureRenderer: RenderConfigurer = { }): TemplateRenderAssert {
  val template: LTemplate
  val renderer: LRenderer
  try {
    val parser = provider.createParser().reconfigure(configureParser)
    renderer = provider.createRenderer(parser, provider.defaultRenderSettings.reconfigure(configureRenderer))
    template = parser.createTestTemplate()
  } catch (e: Exception) {
    return TemplateRenderAssert(error = e)
  }

  return executeTemplateAndAssert(template, renderer, data)
}

fun executeTemplateAndAssert(template: LTemplate, engine: LRenderer, data: Any? = null): TemplateRenderAssert {
  return try {
    val context = engine.createRenderContext(Locale.US, ZoneId.systemDefault(), data)
    val results = engine.executeWithContext(template, context)


    TemplateRenderAssert(template = template,
        renderResult = results,
        context = context)
  } catch (e: Exception) {
    TemplateRenderAssert(template = template, error = e)
  }
}

fun renderTemplateAndAssert(template: LTemplate, engine: LRenderer, data: Any? = null): TemplateRenderAssert {
  return try {
    val context = engine.createRenderContext(Locale.US, ZoneId.systemDefault(), data)
    val results = engine.renderWithContext(template, context)

    TemplateRenderAssert(template = template,
        renderResult = results,
        context = context)
  } catch (e: Exception) {
    TemplateRenderAssert(template = template, error = e)
  }
}

fun LTemplate.executing(data: Any? = null, renderer: MutableRenderSettings.() -> Unit = {}): TemplateRenderAssert {
  return executeTemplateAndAssert(this, createTestRenderer(renderer), data)
}

fun LTemplate.rendering(data: Any? = null, renderer: MutableRenderSettings.() -> Unit = {}): TemplateRenderAssert {
  return renderTemplateAndAssert(this, createTestRenderer(renderer), data.parseIfNecessary())
}

fun LTemplate.assertThat(): TemplateAssert {
  return TemplateAssert(this)
}

fun LFilter.assertThat(name: String? = null): FilterAssert {
  return FilterAssert(name = name, filter = this)
}

inline fun <reified T : Any> T?.asserting(): Assert<T> {
  return when (this) {
    null -> {
      fail("Expected non-null value for ${T::class.qualifiedName}")
      TODO()
    }
    else -> assertk.assert(this)
  }
}

@JvmOverloads fun createTestRenderer(block: MutableRenderSettings.() -> Unit = {}): LRenderer {
  return provider.createRenderer(provider.createParser(), provider.defaultRenderSettings.reconfigure(block))
}

fun createParseSettings(): MutableParseSettings {
  return Liquify.provider.defaultParseSettings.toMutableSettings()
}

fun <K,V> mapOf(key:K, value:V) = mapOf(key to value)

@JvmOverloads fun createTestParser(block: MutableParseSettings.() -> Unit = {}): LParser {
  return provider.createParser(provider.defaultParseSettings.reconfigure(block))
}

fun MutableParseSettings.toParser(): LParser = build().toParser()

fun <T : Any> assertThat(subject: T?): Assert<T> {
  return when (subject) {
    null -> {
      fail("Expected non-null value")
      TODO()
    }
    else -> assertk.assert(subject)
  }
}

inline fun <reified T : Any> T?.assertNullable(): Assert<T?> {
  return assertk.assert(this)
}

