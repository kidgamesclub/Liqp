package liqp

import liqp.config.LRenderSettings
import liqp.config.MutableRenderSettings
import liqp.config.RenderSettings
import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.lookup.PropertyAccessors
import liqp.node.LTemplate
import liqp.nodes.RenderContext
import java.time.ZoneId
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

typealias executeTemplate = (LTemplate, LContext) -> Any?

data class LiquidRenderer @JvmOverloads constructor(val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                                                    override val renderSettings: LRenderSettings,
                                                    val parser: LParser,
                                                    val logic: LLogic = strictLogic) : LRenderer {

  override fun reconfigure(block: MutableRenderSettings.() -> Unit): LRenderer {
    return this.copy(renderSettings = renderSettings.reconfigure(block))
  }

  init {
    if (renderSettings.maxRenderTimeMillis != Long.MAX_VALUE && renderSettings.executor == null) {
      throw Exception("You specified a value for maxRenderTime, but didn't provide an executor")
    }
  }

  private val executeTemplate: executeTemplate = when (renderSettings.executor) {
    null -> inthread@{ template, context -> return@inthread template.rootNode.render(context) }
    else -> executor@{ template, context ->
      val future = renderSettings.executor!!.submit(Callable task@{
        return@task template.rootNode.render(context)
      })

      return@executor future.get(renderSettings.maxRenderTimeMillis, TimeUnit.MILLISECONDS)
    }
  }

  override fun withParser(parser: LParser): LRenderer {
    return when (parser == this.parser) {
      true -> this
      false -> this.copy(parser = parser)
    }
  }

  override fun withExecutorService(executor: ExecutorService): LiquidRenderer = this.copy(renderSettings = renderSettings.toMutableSettings().build {
    this.executor = executor
  })

  override fun withRenderSettings(settings: RenderSettings): LiquidRenderer = this.copy(renderSettings = settings)

  override fun withRenderSettings(block: MutableRenderSettings.() -> Unit): LiquidRenderer {
    val builder = renderSettings.toMutableSettings().apply(block)
    return withRenderSettings(builder.build())
  }

  override fun createRenderContext(locale: Locale, timezone: ZoneId, inputData: Any?): RenderContext {
    return RenderContext(
        inputData = inputData,
        locale = locale,
        timezone = timezone,
        accessors = this.accessors,
        parser = this.parser,
        renderer = this,
        logic = logic)
  }

  override fun execute(template: LTemplate, locale: Locale, timezone: ZoneId, inputData: Any?): Any? = executeTemplate(template,
      createRenderContext(locale, timezone, inputData))

  override fun executeWithContext(template: LTemplate, context: LContext): Any? = executeTemplate(template, context)
  override fun renderWithContext(template: LTemplate, context: LContext): String = executeTemplate(template, context).liquify(context)

  override fun render(template: LTemplate, locale: Locale, timezone: ZoneId, inputData: Any?): String {
    val context = createRenderContext(locale, timezone, inputData)
    return executeTemplate(template, context).liquify(context)
  }

  override fun getAccessor(lContext: LContext, prototype: Any, prop: String): Getter<Any> = accessors.getAccessor(prototype, prop)
}

fun String.checkSize(maxSize: Int): String {
  if (this.length > maxSize) {
    throw LiquidRenderingException("rendered content too large: $maxSize")
  }
  return this
}

private fun Any?.liquify(context: LContext): String {
  return context.asString(this)?.checkSize(context.renderSettings.maxSizeRenderedString) ?: ""
}


