package liqp

import java.util.concurrent.ExecutorService

data class RenderSettings(var strictVariables: Boolean = false,
                          var maxIterations: Int = Integer.MAX_VALUE,
                          var maxStackSize: Int = 100,
                          var maxSizeRenderedString: Int = Integer.MAX_VALUE,
                          var maxRenderTimeMillis: Long = Long.MAX_VALUE,
                          var executor: ExecutorService? = null) {

  fun strictVariables(strictVariables: Boolean): RenderSettings {
    this.strictVariables = strictVariables
    return this
  }

  fun maxIterations(maxIterations: Int): RenderSettings {
    this.maxIterations = maxIterations
    return this
  }

  fun maxStackSize(maxStackSize: Int): RenderSettings {
    this.maxStackSize = maxStackSize
    return this
  }

  fun maxSizeRenderedString(maxSizeRenderedString: Int): RenderSettings {
    this.maxSizeRenderedString = maxSizeRenderedString
    return this
  }

  fun maxRenderTimeMillis(maxRenderTimeMillis: Long): RenderSettings {
    this.maxRenderTimeMillis = maxRenderTimeMillis
    return this
  }

  fun executor(executor: ExecutorService): RenderSettings {
    this.executor = executor
    return this
  }
}
