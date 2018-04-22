package liqp

import liqp.parser.Flavor
import java.io.File
import java.util.concurrent.ExecutorService
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

interface RenderSettingsSpec {
  val isStrictVariables: Boolean
  val maxIterations: Int
  val maxStackSize: Int
  val isUseTruthyChecks: Boolean
  val baseDir: File
  val flavor: Flavor
  val includesDir:File
  val maxSizeRenderedString: Int
  val maxRenderTimeMillis: Long
  val executor: ExecutorService?

  fun toMutableRenderSettings(): MutableRenderSettings
}

data class RenderSettings(override val isStrictVariables: Boolean = false,
                          override val maxIterations: Int = Integer.MAX_VALUE,
                          override val maxStackSize: Int = 100,
                          override val isUseTruthyChecks: Boolean = false,
                          override val baseDir: File,
                          override val flavor: Flavor,
                          override val maxSizeRenderedString: Int = Integer.MAX_VALUE,
                          override val maxRenderTimeMillis: Long = Long.MAX_VALUE,
                          override val executor: ExecutorService? = null) : RenderSettingsSpec {

  override val includesDir: File = baseDir.resolve(flavor.includesDirName)

  override fun toMutableRenderSettings(): MutableRenderSettings {
    return MutableRenderSettings(this)
  }
}

data class MutableRenderSettings(internal var settings: RenderSettings = RenderSettings(baseDir=File("./"), flavor = Flavor.LIQUID)) : RenderSettingsSpec {

  override var isStrictVariables by CopyOnWriteDelegate(settings::isStrictVariables, this::withStrictVariables)
  override var baseDir by CopyOnWriteDelegate(settings::baseDir, this::withBaseDir)
  override var flavor by CopyOnWriteDelegate(settings::flavor, this::withFlavor)
  override var maxIterations by CopyOnWriteDelegate(settings::maxIterations, this::withMaxIterations)
  override var maxStackSize by CopyOnWriteDelegate(settings::maxStackSize, this::withMaxStackSize)
  override var isUseTruthyChecks by CopyOnWriteDelegate(settings::isUseTruthyChecks, this::withUseTruthyChecks)
  override var maxSizeRenderedString by CopyOnWriteDelegate(settings::maxSizeRenderedString, this::withMaxSizeRenderedString)
  override var maxRenderTimeMillis by CopyOnWriteDelegate(settings::maxRenderTimeMillis, this::withMaxRenderTimeMillis)
  override var executor by CopyOnWriteDelegate(settings::executor, this::withExecutor)

  override val includesDir: File
    get() = settings.includesDir

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

  fun withFlavor(flavor: Flavor): MutableRenderSettings {
    settings = settings.copy(flavor = flavor)
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

class CopyOnWriteDelegate<T>(val getter: KProperty0<T>,
                             val setter: KFunction1<T, MutableRenderSettings>) : ReadWriteProperty<MutableRenderSettings, T> {
  override fun getValue(thisRef: MutableRenderSettings, property: KProperty<*>): T {
    return getter.get()
  }

  override fun setValue(thisRef: MutableRenderSettings, property: KProperty<*>, value: T) {
    setter(value)
  }
}
