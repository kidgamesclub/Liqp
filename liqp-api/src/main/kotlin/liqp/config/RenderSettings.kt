package liqp.config

import java.io.File
import java.util.concurrent.ExecutorService

interface RenderSettingsSpec {
  val isStrictVariables: Boolean
  val maxIterations: Int
  val maxStackSize: Int
  val isUseTruthyChecks: Boolean
  val baseDir: File
  val includesDir:File
  val maxSizeRenderedString: Int
  val maxRenderTimeMillis: Long
  val executor: ExecutorService?

  fun toMutableRenderSettings(): MutableRenderSettings
}

data class RenderSettings(override val baseDir: File,
                          override val includesDir: File,
                          override val isStrictVariables: Boolean = false,
                          override val maxIterations: Int = Integer.MAX_VALUE,
                          override val maxStackSize: Int = 100,
                          override val isUseTruthyChecks: Boolean = false,
                          override val maxSizeRenderedString: Int = Integer.MAX_VALUE,
                          override val maxRenderTimeMillis: Long = Long.MAX_VALUE,
                          override val executor: ExecutorService? = null) : RenderSettingsSpec {

  override fun toMutableRenderSettings(): MutableRenderSettings {
    return MutableRenderSettings(this)
  }
}

data class MutableRenderSettings(internal var settings: RenderSettings = RenderSettings(
    baseDir = File("./"),
    includesDir = File("./snippets"))) : RenderSettingsSpec {

  override var isStrictVariables by delegate(settings::isStrictVariables, this::withStrictVariables)
  override var baseDir by delegate(settings::baseDir, this::withBaseDir)
  override var includesDir by delegate(settings::includesDir, this::withIncludesDir)
  override var maxIterations by delegate(settings::maxIterations, this::withMaxIterations)
  override var maxStackSize by delegate(settings::maxStackSize, this::withMaxStackSize)
  override var isUseTruthyChecks by delegate(settings::isUseTruthyChecks, this::withUseTruthyChecks)
  override var maxSizeRenderedString by delegate(settings::maxSizeRenderedString, this::withMaxSizeRenderedString)
  override var maxRenderTimeMillis by delegate(settings::maxRenderTimeMillis, this::withMaxRenderTimeMillis)
  override var executor by delegate(settings::executor, this::withExecutor)

  fun withStrictVariables(strictVariables: Boolean): MutableRenderSettings {
    settings = settings.copy(isStrictVariables = strictVariables)
    return this
  }

  fun withMaxIterations(maxIterations: Int): MutableRenderSettings {
    settings = settings.copy(maxIterations = maxIterations)
    return this
  }

  fun withMaxStackSize(maxStackSize: Int): MutableRenderSettings {
    settings = settings.copy(maxStackSize = maxStackSize)
    return this
  }

  fun withUseTruthyChecks(isUseTruthyChecks: Boolean): MutableRenderSettings {
    settings = settings.copy(isUseTruthyChecks = isUseTruthyChecks)
    return this
  }

  fun withBaseDir(baseDir: File): MutableRenderSettings {
    settings = settings.copy(baseDir = baseDir)
    return this
  }

  fun withIncludesDir(includesDir: File): MutableRenderSettings {
    settings = settings.copy(includesDir = includesDir)
    return this
  }

  fun withMaxSizeRenderedString(maxSizeRenderedString: Int): MutableRenderSettings {
    settings = settings.copy(maxSizeRenderedString = maxSizeRenderedString)
    return this
  }

  fun withMaxRenderTimeMillis(maxRenderTimeMillis: Long): MutableRenderSettings {
    settings = settings.copy(maxRenderTimeMillis = maxRenderTimeMillis)
    return this
  }

  fun withExecutor(executor: ExecutorService?): MutableRenderSettings {
    settings = settings.copy(executor = executor)
    return this
  }

  override fun toMutableRenderSettings(): MutableRenderSettings {
    return this
  }

  fun build(): RenderSettings {
    return settings
  }
}

