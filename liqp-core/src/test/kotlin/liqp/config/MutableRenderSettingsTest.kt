package liqp.config

import assertk.all
import assertk.assertThat
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
    assertThat(settings).all {
      assertThat(settings.baseDir).isEqualTo(File("ttt"))
      assertThat(settings.defaultDateFormat).isEqualTo('d')
      assertThat(settings.executor).isNotNull()
      assertThat(settings.includesDir).isEqualTo("ink")
      assertThat(settings.maxIterations).isEqualTo(345)
      assertThat(settings.maxRenderTimeMillis).isEqualTo(444L)
      assertThat(settings.maxStackSize).isEqualTo(334)
      assertThat(settings.isStrictIncludes).isEqualTo(true)
      assertThat(settings.isStrictVariables).isEqualTo(true)
      assertThat(settings.isUseTruthyChecks).isEqualTo(true)
      assertThat(settings.defaultLocale).isEqualTo(Locale.US)
      assertThat(settings.defaultTimezone).isEqualTo(ZoneId.systemDefault())
      assertThat(settings.isUseTruthyChecks).isEqualTo(true)
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
    assertThat(settings).all {
      assertThat(settings.baseDir).isEqualTo(File("ttt"))
      assertThat(settings.defaultDateFormat).isEqualTo('d')
      assertThat(settings.executor).isNotNull()
      assertThat(settings.includesDir).isEqualTo("ink")
      assertThat(settings.maxIterations).isEqualTo(345)
      assertThat(settings.maxRenderTimeMillis).isEqualTo(444L)
      assertThat(settings.maxStackSize).isEqualTo(334)
      assertThat(settings.isStrictIncludes).isEqualTo(true)
      assertThat(settings.isStrictVariables).isEqualTo(true)
      assertThat(settings.defaultLocale).isEqualTo(Locale.US)
      assertThat(settings.defaultTimezone).isEqualTo(ZoneId.systemDefault())
      assertThat(settings.isUseTruthyChecks).isEqualTo(true)
    }
  }
}
