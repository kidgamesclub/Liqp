package liqp

import liqp.config.MutableRenderSettings
import liqp.config.RenderSettings
import liqp.config.RenderSettingsSpec
import liqp.lookup.PropertyAccessors
import liqp.node.LTemplate
import liqp.nodes.RenderContext
import liqp.parser.Flavor
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

typealias executeTemplate = (LTemplate, RenderContext) -> Any?

val defaultRenderSettings = RenderSettings(defaultParseSettings.baseDir, defaultParseSettings.includesDir)

data class LiquidRenderer
@JvmOverloads constructor(val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                          val parser: LiquidParser = LiquidParser(),
                          val settings: RenderSettings = defaultRenderSettings): RenderSettingsSpec by settings {

  init {
    if (maxRenderTimeMillis != Long.MAX_VALUE && executor == null) {
      throw Exception("You specified a value for maxRenderTime, but didn't provide an executor")
    }
  }

  companion object {
    @JvmStatic
    val defaultInstance = newInstance()

    @JvmStatic
    fun newInstance(settings: RenderSettings = defaultRenderSettings): LiquidRenderer {
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
      val future = executor.submit(Callable<Any?> task@{
        return@task template.rootNode.render(context)
      })

      return@executor future.get(this.maxRenderTimeMillis, TimeUnit.MILLISECONDS)
    }
  }

  fun withTemplateFactory(factory: LiquidParser): LiquidRenderer {
    return when (factory == this.parser) {
      true -> this
      false -> this.copy(parser = factory)
    }
  }

  fun withExecutorService(executor: ExecutorService): LiquidRenderer {
    return this.copy(settings = settings.copy(executor = executor))
  }

  fun withRenderSettings(settings: RenderSettings): LiquidRenderer {
    return this.copy(settings = settings)
  }

  fun withRenderSettings(callback: (MutableRenderSettings) -> MutableRenderSettings): LiquidRenderer {
    val builder = settings.toMutableRenderSettings()
    callback.invoke(builder)
    return withRenderSettings(builder.build())
  }

  fun createRenderContext(inputData: Any?): RenderContext {
    return RenderContext(
        rawInputData = inputData,
        accessors = this.accessors,
        parser = this.parser,
        renderer = this,
        logic = logic)
  }

  lateinit var logic:LLogic

  @JvmOverloads
  fun execute(template: Template, inputData: Any? = null): Any? {
    return executeTemplate(template, createRenderContext(inputData))
  }

  fun executeWithContext(template: Template, context: RenderContext): Any? {
    return executeTemplate(template, context)
  }

  fun render(template: LTemplate, context: RenderContext): String {
    val result = executeTemplate(template, context)
    return result.toNonNullString()
  }

  @JvmOverloads
  fun render(template: Template, inputData: Any? = null): String {
    val result = executeTemplate(template, createRenderContext(inputData))
    return result.toNonNullString()
  }
}


