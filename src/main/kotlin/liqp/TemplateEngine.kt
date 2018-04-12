package liqp

import liqp.lookup.PropertyAccessors
import liqp.nodes.RenderContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

typealias executeTemplate = (Template, RenderContext) -> Any?

data class TemplateEngine(val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                          val executor: ExecutorService? = null,
                          val templateFactory: TemplateFactory = TemplateFactory(),
                          val isStrictVariables: Boolean = false,
                          val maxStackSize: Int = 100,
                          val maxSizeRenderedString: Int = Integer.MAX_VALUE,
                          val maxIterations: Int = Integer.MAX_VALUE,
                          val maxRenderTime: Long = Long.MAX_VALUE) {

  init {
    if (maxRenderTime != Long.MAX_VALUE && executor == null) {
      throw Exception("You specified a value for maxRenderTime, but didn't provide an executor")
    }
  }

  companion object {
    @JvmStatic
    val defaultInstance = newInstance()

    @JvmStatic
    fun newInstance(settings: RenderSettings = RenderSettings()): TemplateEngine {
      return TemplateEngine(isStrictVariables = settings.strictVariables,
          maxStackSize = settings.maxStackSize,
          maxSizeRenderedString = settings.maxSizeRenderedString,
          maxIterations = settings.maxIterations,
          executor = settings.executor,
          maxRenderTime = settings.maxRenderTimeMillis)
    }

    fun newInstance(configure: RenderSettings.() -> Any?): TemplateEngine {
      val renderSettings = RenderSettings()
      renderSettings.configure()
      return newInstance(renderSettings)
    }
  }

  private val executeTemplate: executeTemplate = when (executor) {
    null -> { template, context -> template.rootNode.render(context) }
    else -> { template, context ->
      val future = executor.submit({
        template.rootNode.render(context)
      })

      future.get(this.maxRenderTime, TimeUnit.MILLISECONDS)
    }
  }

  fun withTemplateFactory(factory: TemplateFactory): TemplateEngine {
    return when (factory == this.templateFactory) {
      true -> this
      false -> TemplateEngine(accessors = this.accessors, executor = this.executor,
          templateFactory = factory, maxIterations = this.maxIterations,
          maxRenderTime = this.maxRenderTime)
    }
  }

  fun withExecutorService(service: ExecutorService): TemplateEngine {
    return this.copy(executor = service)
  }

  fun withRenderSettings(settings: RenderSettings): TemplateEngine {
    return this.copy(maxStackSize = settings.maxIterations,
        maxRenderTime = settings.maxRenderTimeMillis,
        maxIterations = settings.maxIterations,
        maxSizeRenderedString = settings.maxSizeRenderedString)
  }

  fun withRenderSettings(callback: (RenderSettings) -> RenderSettings): TemplateEngine {
    val builder = RenderSettings(isStrictVariables, maxIterations, maxStackSize, this.maxSizeRenderedString,
        this.maxRenderTime, this.executor)
    callback.invoke(builder)
    return withRenderSettings(builder)
  }

  fun createRenderContext(inputData: Any?): RenderContext {
    return RenderContext(inputData, accessors, maxIterations, templateFactory, this,
        isStrictVariables, maxStackSize, maxSizeRenderedString)
  }

  @JvmOverloads
  fun execute(template: Template, inputData: Any? = null): Any? {
    return executeTemplate(template, createRenderContext(inputData))
  }

  fun executeWithContext(template: Template, context: RenderContext): Any? {
    return executeTemplate(template, context)
  }

  fun render(template: Template, context: RenderContext): String {
    val result = executeTemplate(template, context)
    return asString(result)
  }

  @JvmOverloads
  fun render(template: Template, inputData: Any? = null): String {
    val result = executeTemplate(template, createRenderContext(inputData))
    return asString(result)
  }

  private fun asString(result: Any?): String {
    return when (result) {
      null -> return ""
      else -> result.toString()
    }
  }
}


