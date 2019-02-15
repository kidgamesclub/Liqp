package liqp

import liqp.config.LParseSettings
import liqp.config.LRenderSettings
import liqp.config.MutableParseSettings
import liqp.config.MutableRenderSettings
import liqp.config.RenderSettings
import liqp.context.LContext
import liqp.node.LTemplate
import java.io.File
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ExecutorService

val provider = Liquify.provider

data class LiquidEngine(private var internalParseSettings: LParseSettings = provider.defaultParseSettings,
                        private var internalRenderSettings: LRenderSettings = provider.defaultRenderSettings,
                        private var internalParser: LParser = internalParseSettings.toParser(),
                        private var internalRenderer: LRenderer = provider.createRenderer(internalParser, internalRenderSettings)) : LEngine, LParser, LRenderer {
  override val parser: LParser get() = internalParser
  override val renderer: LRenderer get() = internalRenderer
  override val parseSettings: LParseSettings get() = internalParseSettings
  override val renderSettings: LRenderSettings get() = internalRenderSettings

  override fun parse(template: String): LTemplate = parser.parse(template)
  override fun parseFile(file: File): LTemplate = parser.parseFile(file)
  override fun toRenderSettings(): LRenderSettings = parser.toRenderSettings()
  override fun withParser(parser: LParser): LRenderer = renderer.withParser(parser)
  override fun withExecutorService(executor: ExecutorService): LRenderer = renderer.withExecutorService(executor)
  override fun withRenderSettings(settings: RenderSettings): LRenderer = renderer.withRenderSettings(settings)
  override fun withRenderSettings(block: MutableRenderSettings.() -> Unit): LRenderer = renderer.withRenderSettings(block)
  override fun createRenderContext(locale: Locale, timezone: ZoneId, inputData: Any?): LContext = renderer.createRenderContext(locale, timezone, inputData)
  override fun execute(template: LTemplate, locale: Locale, timezone: ZoneId, inputData: Any?): Any? = renderer.execute(template, locale, timezone, inputData)
  override fun render(template: LTemplate, locale: Locale, timezone: ZoneId, inputData: Any?): String = renderer.render(template, locale, timezone, inputData)
  override fun executeWithContext(template: LTemplate, context: LContext): Any? = renderer.executeWithContext(template, context)
  override fun renderWithContext(template: LTemplate, context: LContext): String = renderer.renderWithContext(template, context)
  override fun getAccessor(lContext: LContext, prototype: Any, prop: String): Getter<Any> = renderer.getAccessor(lContext, prototype, prop)
  override fun reconfigure(block: MutableRenderSettings.() -> Unit): LRenderer = renderer.reconfigure(block)

  override fun reset() {
    internalParseSettings = provider.defaultParseSettings
    internalRenderSettings = provider.defaultRenderSettings
    internalParser = parseSettings.toParser()
    internalRenderer = provider.createRenderer(internalParser, internalRenderSettings)
  }

  override fun configureParser(block: MutableParseSettings.() -> Unit) {
    internalParseSettings = internalParseSettings.reconfigure(block)
    internalParser = internalParseSettings.toParser()
    internalRenderSettings = internalRenderSettings.reconfigure {
      includesDir = internalParseSettings.includesDir
      baseDir = internalParseSettings.baseDir
      isStrictVariables = internalParseSettings.isStrictVariables
      isStrictIncludes = internalParseSettings.isStrictIncludes
    }
    internalRenderer = provider.createRenderer(internalParser, internalRenderSettings)
  }

  override fun configureRenderer(block: MutableRenderSettings.() -> Unit) {
    this.internalRenderSettings = internalRenderSettings.reconfigure(block)
    this.internalParseSettings = internalParseSettings.reconfigure {
      includesDir = internalRenderSettings.includesDir
      baseDir = internalRenderSettings.baseDir
      isStrictVariables = internalRenderSettings.isStrictVariables
      isStrictIncludes = internalRenderSettings.isStrictIncludes
    }
    this.internalParser = internalParseSettings.toParser()
    this.internalRenderer = provider.createRenderer(internalParser, internalRenderSettings)
  }

  companion object {
    @JvmField val liquid = LiquidEngine()
  }
}
