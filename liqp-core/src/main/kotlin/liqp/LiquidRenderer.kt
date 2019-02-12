package liqp

import liqp.config.MutableRenderSettings
import liqp.config.RenderSettings
import liqp.config.LRenderSettings
import liqp.context.LContext
import liqp.exceptions.LiquidRenderingException
import liqp.lookup.PropertyAccessors
import liqp.node.LTemplate
import liqp.nodes.RenderContext
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

typealias executeTemplate = (LTemplate, LContext) -> Any?

val DefaultRenderSettings = RenderSettings(defaultParseSettings)

data class LiquidRenderer @JvmOverloads constructor(val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                          val parser: LParser = LiquidParser(),
                          override val settings: RenderSettings = DefaultRenderSettings,
                          val logic: LLogic = strictLogic) :
    LRenderSettings by settings,
    LRenderer {

  init {
    if (maxRenderTimeMillis != Long.MAX_VALUE && executor == null) {
      throw Exception("You specified a value for maxRenderTime, but didn't provide an executor")
    }
  }

  companion object {
    @JvmStatic
    val defaultInstance = newInstance()

    @JvmStatic
    fun newInstance(settings: RenderSettings = DefaultRenderSettings): LiquidRenderer {
      return LiquidRenderer(settings = settings)
    }

    fun newInstance(configure: MutableRenderSettings.() -> Any?): LiquidRenderer {
      val renderSettings = MutableRenderSettings()
      renderSettings.configure()
      return newInstance(renderSettings.build())
    }
  }

  private val executeTemplate: executeTemplate = when (executor) {
    null -> inthread@{ template, context -> return@inthread template.rootNode.render(context) }
    else -> executor@{ template, context ->
      val future = executor.submit(Callable task@{
        return@task template.rootNode.render(context)
      })

      return@executor future.get(this.maxRenderTimeMillis, TimeUnit.MILLISECONDS)
    }
  }

  override fun withParser(parser: LParser): LRenderer {
    return when (parser == this.parser) {
      true -> this
      false -> this.copy(parser = parser)
    }
  }

  override fun withExecutorService(executor: ExecutorService): LiquidRenderer = this.copy(settings = settings.copy(executor = executor))
  override fun withRenderSettings(settings: RenderSettings): LiquidRenderer = this.copy(settings = settings)

  override fun withRenderSettings(callback: (MutableRenderSettings) -> MutableRenderSettings): LiquidRenderer {
    val builder = settings.toMutableRenderSettings()
    callback.invoke(builder)
    return withRenderSettings(builder.build())
  }

  override fun createRenderContext(inputData: Any?): RenderContext {
    return RenderContext(
        rawInputData = inputData,
        accessors = this.accessors,
        parser = this.parser,
        renderer = this,
        logic = logic)
  }

  override fun execute(template: LTemplate, inputData: Any?): Any? = executeTemplate(template, createRenderContext(inputData))
  override fun executeWithContext(template: LTemplate, context: LContext): Any? = executeTemplate(template, context)
  override fun renderWithContext(template: LTemplate, context: LContext): String = executeTemplate(template, context).liquify(context)
  override fun render(template: LTemplate, context: LContext): String = executeTemplate(template, context).liquify(context)

  override fun render(template: LTemplate, inputData: Any?): String {
    val context = createRenderContext(inputData)
    return executeTemplate(template, context).liquify(context)
  }

  override fun getAccessor(lContext: LContext, prototype: Any, prop: String): Getter<Any> = accessors.getAccessor(lContext, prototype, prop)
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


