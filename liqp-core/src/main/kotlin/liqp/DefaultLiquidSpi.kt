package liqp

import liqp.config.LParseSettings
import liqp.config.LRenderSettings
import liqp.config.ParseSettings
import liqp.filter.Filters
import liqp.parser.Flavor
import liqp.tag.Tags
import java.io.File

class DefaultLiquidSpi: Liquify {

  override val defaultFilters: Filters = LiquidDefaults.defaultFilters
  override val defaultTags: Tags = LiquidDefaults.defaultTags

  override val defaultParseSettings: LParseSettings = ParseSettings(
      defaultTags,
      defaultFilters,
      baseDir = File("./"),
      includesDir = Flavor.LIQUID.includesDirName)

  override fun createParser(parseSettings: LParseSettings): LParser = LiquidParser(parseSettings)
  override fun createRenderer(parser:LParser, renderSettings: LRenderSettings): LRenderer
      = LiquidRenderer(parser = parser, renderSettings = renderSettings)



}
