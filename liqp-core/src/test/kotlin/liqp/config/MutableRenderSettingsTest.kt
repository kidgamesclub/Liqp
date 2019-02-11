package liqp.config

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.Test
import java.io.File
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
    }
  }
}
