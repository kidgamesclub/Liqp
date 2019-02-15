package liqp.config

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.Test
import java.io.File
import java.time.ZoneId
import java.util.*
import java.util.concurrent.Executors

class MutableRenderSettingsTest {
  @Test fun testBuilder() {
    val settings = MutableRenderSettings()
        .withBaseDir(File("ttt"))
        .withDefaultDateFormat('d')
        .withExecutor(Executors.newSingleThreadExecutor())
        .withIncludesDir("ink")
        .withMaxIterations(345)
        .withMaxRenderTimeMillis(444L)
        .withMaxStackSize(334)
        .withStrictIncludes(true)
        .withStrictVariables(true)
        .withDefaultLocale(Locale.US)
        .withDefaultTimezone(ZoneId.systemDefault())
        .withUseTruthyChecks(true)
        .build()
    assert(settings).all {
      assert(settings.baseDir).isEqualTo(File("ttt"))
      assert(settings.defaultDateFormat).isEqualTo('d')
      assert(settings.executor).isNotNull()
      assert(settings.includesDir).isEqualTo("ink")
      assert(settings.maxIterations).isEqualTo(345)
      assert(settings.maxRenderTimeMillis).isEqualTo(444L)
      assert(settings.maxStackSize).isEqualTo(334)
      assert(settings.isStrictIncludes).isEqualTo(true)
      assert(settings.isStrictVariables).isEqualTo(true)
      assert(settings.isUseTruthyChecks).isEqualTo(true)
      assert(settings.defaultLocale).isEqualTo(Locale.US)
      assert(settings.defaultTimezone).isEqualTo(ZoneId.systemDefault())
      assert(settings.isUseTruthyChecks).isEqualTo(true)
    }
  }

  @Test fun testBuilder_Properties() {
    val settings = MutableRenderSettings().build {

      baseDir = File("ttt")
      defaultDateFormat = 'd'
      executor = Executors.newSingleThreadExecutor()
      includesDir = "ink"
      maxIterations = 345
      maxRenderTimeMillis = 444L
      maxStackSize = 334
      isStrictIncludes = true
      isStrictVariables = true
      isUseTruthyChecks = true
      defaultTimezone = ZoneId.systemDefault()
      defaultLocale = Locale.US
    }
    assert(settings).all {
      assert(settings.baseDir).isEqualTo(File("ttt"))
      assert(settings.defaultDateFormat).isEqualTo('d')
      assert(settings.executor).isNotNull()
      assert(settings.includesDir).isEqualTo("ink")
      assert(settings.maxIterations).isEqualTo(345)
      assert(settings.maxRenderTimeMillis).isEqualTo(444L)
      assert(settings.maxStackSize).isEqualTo(334)
      assert(settings.isStrictIncludes).isEqualTo(true)
      assert(settings.isStrictVariables).isEqualTo(true)
      assert(settings.defaultLocale).isEqualTo(Locale.US)
      assert(settings.defaultTimezone).isEqualTo(ZoneId.systemDefault())
      assert(settings.isUseTruthyChecks).isEqualTo(true)
    }
  }
}
