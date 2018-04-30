package liqp

import liqp.config.ParseSettings
import liqp.filter.Filters
import liqp.tag.Tags
import java.util.*

interface Liquify {
  fun createParser(settings: ParseSettings): LParser
  val defaultFilters: Filters
  val defaultTags: Tags

  companion object {
    val provider: Liquify by lazy {
      ServiceLoader.load(Liquify::class.java).first()
    }
  }
}


