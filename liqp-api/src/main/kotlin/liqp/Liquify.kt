package liqp

import liqp.config.LParseSettings
import liqp.config.LRenderSettings
import liqp.config.MutableParseSettings
import liqp.config.RenderSettings
import liqp.filter.Filters
import liqp.tag.Tags
import java.util.*
import java.util.function.Consumer

interface Liquify {
  fun createParser(parseSettings: LParseSettings): LParser
  fun createParser(): LParser = createParser(defaultParseSettings)
  fun createParser(configure: MutableParseSettings.() -> Unit): LParser = createParser(defaultParseSettings.reconfigure(configure))
  fun createParserJvm(configure: Consumer<MutableParseSettings>): LParser = createParser(defaultParseSettings.reconfigure { configure.accept(this) })
  fun createRenderer(parser: LParser, renderSettings: LRenderSettings = defaultRenderSettings): LRenderer

  val defaultRenderSettings: LRenderSettings get() = RenderSettings(defaultParseSettings)
  val defaultParseSettings: LParseSettings

  val defaultFilters: Filters
  val defaultTags: Tags

  companion object {
    @JvmStatic
    val provider: Liquify by lazy {
      ServiceLoader.load(Liquify::class.java).firstOrNull()
          ?: throw Error("No Liquify instance could be determined")
    }
  }
}


