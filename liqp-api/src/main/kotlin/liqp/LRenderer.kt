package liqp

import liqp.config.LRenderSettings
import liqp.config.MutableRenderSettings
import liqp.config.RenderSettings
import liqp.context.LContext
import liqp.node.LTemplate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ExecutorService

interface LRenderer {
  val renderSettings: LRenderSettings
  fun withParser(parser: LParser): LRenderer
  fun withExecutorService(executor: ExecutorService): LRenderer
  fun withRenderSettings(settings: RenderSettings): LRenderer
  fun withRenderSettings(block: MutableRenderSettings.() -> Unit): LRenderer
  fun createRenderContext(locale: Locale, timezone: ZoneId, inputData: Any?): LContext
  fun execute(template: LTemplate, locale: Locale, timezone: ZoneId, inputData: Any? = null): Any?
  fun render(template: LTemplate,  locale: Locale, timezone: ZoneId, inputData: Any? = null): String
  fun executeWithContext(template: LTemplate, context: LContext): Any?
  fun renderWithContext(template: LTemplate, context: LContext): String
  fun getAccessor(lContext: LContext, prototype: Any, prop: String): Getter<Any>
  fun reconfigure(block: MutableRenderSettings.() -> Unit): LRenderer
}
