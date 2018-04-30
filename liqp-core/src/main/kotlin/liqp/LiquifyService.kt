package liqp

import liqp.config.ParseSettings
import liqp.filter.Filters
import liqp.tag.Tags

class LiquifyService: Liquify {
  override fun createParser(settings: ParseSettings): LParser = LiquidParser(settings)
  override val defaultFilters: Filters = LiquidDefaults.getDefaultFilters()
  override val defaultTags: Tags = LiquidDefaults.getDefaultTags()
}
