package liqp

import liqp.lookup.PropertyAccessors
import liqp.nodes.RenderContext
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

typealias executeTemplate = (Template, RenderContext) -> Any?

data class TemplateEngine(val accessors: PropertyAccessors = PropertyAccessors.newInstance(),
                                           val executor: ExecutorService? = null,
                                           val templateFactory: TemplateFactory = TemplateFactory(),
                                           val isStrictVariables: Boolean = false,
                                           val isTruthy: Boolean = true,
                                           val maxStackSize: Int = 100,
                                           val maxSizeRenderedString: Int = Integer.MAX_VALUE,
                                           val maxIterations: Int = Integer.MAX_VALUE,
                                           val maxRenderTimeMillis: Long = Long.MAX_VALUE) {

  init {
    if (maxRenderTimeMillis != Long.MAX_VALUE && executor == null) {
      throw Exception("You specified a value for maxRenderTime, but didn't provide an executor")
    }
  }

  companion object {
    @JvmStatic
    val defaultInstance = newInstance()

    @JvmStatic
    fun newInstance(settings: RenderSettings = RenderSettings()): TemplateEngine {
      return TemplateEngine(isStrictVariables = settings.isStrictVariables,
          maxStackSize = settings.maxStackSize,
          maxSizeRenderedString = settings.maxSizeRenderedString,
          isTruthy = settings.isTruthy,
          maxIterations = settings.maxIterations,
          executor = settings.executor,
          maxRenderTimeMillis = settings.maxRenderTimeMillis)
    }

    fun newInstance(configure: RenderSettings.() -> Any?): TemplateEngine {
      val renderSettings = RenderSettings()
      renderSettings.configure()
      return newInstance(renderSettings)
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

  fun withTemplateFactory(factory: TemplateFactory): TemplateEngine {
    return when (factory == this.templateFactory) {
      true -> this
      false -> this.copy(templateFactory=factory)
    }
  }

  fun withExecutorService(service: ExecutorService): TemplateEngine {
    return this.copy(executor = service)
  }

  fun withRenderSettings(settings: RenderSettings): TemplateEngine {
    return this.copy(maxStackSize = settings.maxIterations,
        maxRenderTimeMillis = settings.maxRenderTimeMillis,
        maxIterations = settings.maxIterations,
        isTruthy = settings.isTruthy,
        isStrictVariables = settings.isStrictVariables,
        maxSizeRenderedString = settings.maxSizeRenderedString)
  }

  fun withRenderSettings(callback: (RenderSettings) -> RenderSettings): TemplateEngine {
    val builder = RenderSettings(
        isStrictVariables = this.isStrictVariables,
        maxIterations = this.maxIterations,
        maxStackSize = this.maxStackSize,
        maxSizeRenderedString = this.maxSizeRenderedString,
        maxRenderTimeMillis = this.maxRenderTimeMillis,
        executor = this.executor)
    callback.invoke(builder)
    return withRenderSettings(builder)
  }

  fun createRenderContext(inputData: Any?): RenderContext {
    return RenderContext(
        inputData=inputData,
        accessors = this.accessors,
        maxIterations = this.maxIterations,
        templateFactory = this.templateFactory,
        engine = this,
        isStrictVariables = this.isStrictVariables,
        isUseTruthyChecks = this.isTruthy,
        maxStackSize = this.maxStackSize,
        maxSizeRenderedString = this.maxSizeRenderedString)
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
    return result.toNonNullString()
  }

  @JvmOverloads
  fun render(template: Template, inputData: Any? = null): String {
    val result = executeTemplate(template, createRenderContext(inputData))
    return result.toNonNullString()
  }

}


