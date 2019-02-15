package liqp.config

import liqp.parser.Flavor.LIQUID
import java.io.File
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ExecutorService

interface LRenderSettings {
  val isStrictVariables: Boolean
  val isStrictIncludes: Boolean
  val maxIterations: Int
  val maxStackSize: Int
  val isUseTruthyChecks: Boolean
  val baseDir: File
  val includesDir: String
  val maxSizeRenderedString: Int
  val maxRenderTimeMillis: Long
  val defaultDateFormat: Char
  val defaultLocale: Locale
  val defaultTimezone: ZoneId
  val executor: ExecutorService?
  fun toMutableSettings(): MutableRenderSettings
  fun reconfigure(block: MutableRenderSettings.() -> Unit): LRenderSettings = toMutableSettings().build(block)
}

data class RenderSettings(override val baseDir: File,
                          override val includesDir: String,
                          override val isStrictVariables: Boolean = false,
                          override val isStrictIncludes: Boolean,
                          override val maxIterations: Int = Integer.MAX_VALUE,
                          override val maxStackSize: Int = 100,
                          override val isUseTruthyChecks: Boolean = false,
                          override val maxSizeRenderedString: Int = Integer.MAX_VALUE,
                          override val maxRenderTimeMillis: Long = Long.MAX_VALUE,
                          override val defaultDateFormat: Char = 'c',
                          override val executor: ExecutorService? = null,
                          override val defaultLocale: Locale = Locale.US,
                          override val defaultTimezone: ZoneId = ZoneId.of("America/Phoenix")) : LRenderSettings {

  constructor(parseSettings: LParseSettings) : this(baseDir = parseSettings.baseDir,
      includesDir = parseSettings.includesDir,
      isStrictVariables = parseSettings.isStrictVariables,
      isStrictIncludes = parseSettings.isStrictIncludes)

  override fun toMutableSettings(): MutableRenderSettings {
    return MutableRenderSettings(this)
  }
}

data class MutableRenderSettings(internal var settings: RenderSettings = RenderSettings(
    baseDir = File("./"),
    includesDir = LIQUID.includesDirName,
    isStrictIncludes = false)) {

  var isStrictVariables by delegate(settings::isStrictVariables, this::withStrictVariables)
  var isStrictIncludes by delegate(settings::isStrictIncludes, this::withStrictIncludes)
  var baseDir by delegate(settings::baseDir, this::withBaseDir)
  var includesDir by delegate(settings::includesDir, this::withIncludesDir)
  var maxIterations by delegate(settings::maxIterations, this::withMaxIterations)
  var maxStackSize by delegate(settings::maxStackSize, this::withMaxStackSize)
  var isUseTruthyChecks by delegate(settings::isUseTruthyChecks, this::withUseTruthyChecks)
  var maxSizeRenderedString by delegate(settings::maxSizeRenderedString, this::withMaxSizeRenderedString)
  var maxRenderTimeMillis by delegate(settings::maxRenderTimeMillis, this::withMaxRenderTimeMillis)
  var defaultDateFormat by delegate(settings::defaultDateFormat, this::withDefaultDateFormat)
  var defaultLocale by delegate(settings::defaultLocale, this::withDefaultLocale)
  var defaultTimezone by delegate(settings::defaultTimezone, this::withDefaultTimezone)
  var executor by delegate(settings::executor, this::withExecutor)

  fun withStrictVariables(strictVariables: Boolean): MutableRenderSettings {
    settings = settings.copy(isStrictVariables = strictVariables)
    return this
  }

  fun withStrictIncludes(strictIncludes: Boolean): MutableRenderSettings {
    settings = settings.copy(isStrictIncludes = strictIncludes)
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

  fun withIncludesDir(includesDir: String): MutableRenderSettings {
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

  fun withDefaultDateFormat(defaultDateFormat: Char): MutableRenderSettings {
    settings = settings.copy(defaultDateFormat = defaultDateFormat)
    return this
  }

  fun withDefaultTimezone(defaultTimezone: ZoneId): MutableRenderSettings = apply {
    settings = settings.copy(defaultTimezone = defaultTimezone)
  }

  fun withDefaultLocale(defaultLocale: Locale): MutableRenderSettings = apply {
    settings = settings.copy(defaultLocale = defaultLocale)
  }

  fun withExecutor(executor: ExecutorService?): MutableRenderSettings {
    settings = settings.copy(executor = executor)
    return this
  }

  fun build(block: MutableRenderSettings.() -> Unit = {}): RenderSettings = apply(block).settings
}

