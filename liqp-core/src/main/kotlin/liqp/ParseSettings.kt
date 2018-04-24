package liqp

import liqp.filters.Filters
import liqp.filters.LFilter
import liqp.parser.Flavor
import liqp.tags.Tag
import liqp.tags.Tags
import java.io.File

interface ParseOperations {
  fun parse(template: String): Template

  fun parseFile(file: File): Template
}

interface ParseSettingsSpec {
  val flavor: Flavor
  val baseDir: File
  val includesDir: File
  val isStrictVariables: Boolean
  val isStripSpacesAroundTags: Boolean
  val isStripSingleLine: Boolean
  val maxTemplateSize: Long?
  val isKeepParseTree: Boolean
  val tags: Tags
  val filters: Filters
  val cacheSettings: CacheSetup?

  fun toMutableParseSettings(): MutableParseSettings
}

data class ParseSettings(override val flavor: Flavor = Flavor.LIQUID,
                         override val baseDir: File = File(""),
                         override val isStrictVariables: Boolean = false,
                         override val isStripSpacesAroundTags: Boolean = false,
                         override val isStripSingleLine: Boolean = false,
                         override val maxTemplateSize: Long? = null,
                         override val isKeepParseTree: Boolean = false,
                         override val tags: Tags = Tags.getDefaultTags(),
                         override val filters: Filters = Filters.getDefaultFilters(),
                         override val cacheSettings: CacheSetup? = null) : ParseSettingsSpec, ParseOperations {

  override val includesDir: File
    get() {
      return baseDir.resolve(flavor.includesDirName)
    }

  fun toParser(): LiquidParser {
    return LiquidParser(this)
  }

  override fun toMutableParseSettings(): MutableParseSettings {
    return MutableParseSettings(this)
  }

  fun withFilters(vararg filter: LFilter): ParseSettings {
    return this.copy(filters = this.filters.withFilters(*filter))
  }

  override fun parse(template: String): Template {
    return LiquidParser(this).parse(template)
  }

  override fun parseFile(file: File): Template {
    return LiquidParser(this).parseFile(file)
  }
}

/**
 * This class is largely to support interop with java builders.  kotlin code should avoid this,
 * and just use the copy methods on {@link RenderSettings} directly
 */
data class MutableParseSettings(var settings: ParseSettings = ParseSettings()) :
    ParseSettingsSpec by settings, ParseOperations {

  fun flavor(flavor: Flavor): MutableParseSettings {
    settings = settings.copy(flavor = flavor)
    return this
  }

  fun baseDir(baseDir: File): MutableParseSettings {
    settings = settings.copy(baseDir = baseDir)
    return this
  }

  fun strictVariables(isStrictVariables: Boolean): MutableParseSettings {
    settings = settings.copy(isStrictVariables = isStrictVariables)
    return this
  }

  fun stripSpacesAroundTags(stripSpacesAroundTags: Boolean): MutableParseSettings {
    settings = settings.copy(isStripSpacesAroundTags = stripSpacesAroundTags)
    return this
  }

  fun stripSingleLine(stripSingleLine: Boolean): MutableParseSettings {
    settings = settings.copy(isStripSingleLine = stripSingleLine)
    return this
  }

  fun maxTemplateSize(maxTemplateSize: Long?): MutableParseSettings {
    settings = settings.copy(maxTemplateSize = maxTemplateSize)
    return this
  }

  fun addTags(vararg tags: Tag): MutableParseSettings {
    settings = settings.copy(tags = settings.tags.withTags(*tags))
    return this
  }

  fun addFilters(vararg filters: LFilter): MutableParseSettings {
    settings = settings.copy(filters = settings.filters.withFilters(*filters))
    return this
  }

  fun tags(tags: Tags): MutableParseSettings {
    settings = settings.copy(tags = tags)
    return this
  }

  fun filters(filters: Filters): MutableParseSettings {
    settings = settings.copy(filters = filters)
    return this
  }

  fun keepParseTree(isKeepParseTree: Boolean): MutableParseSettings {
    settings = settings.copy(isKeepParseTree = isKeepParseTree)
    return this
  }

  fun cacheSettings(cacheSettings: CacheSetup): MutableParseSettings {
    settings = settings.copy(cacheSettings = cacheSettings)
    return this
  }

  fun toParser(): LiquidParser {
    return LiquidParser(settings = settings)
  }

  override fun parse(template: String): Template {
    return LiquidParser(settings = settings).parse(template)
  }

  override fun parseFile(file: File): Template {
    return LiquidParser(settings = settings).parseFile(file)
  }
}

