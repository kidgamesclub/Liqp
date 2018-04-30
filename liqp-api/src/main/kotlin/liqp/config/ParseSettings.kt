package liqp.config

import com.google.common.cache.CacheBuilder
import liqp.LParser
import liqp.Liquify
import liqp.filter.Filters
import liqp.filter.LFilter
import liqp.parser.Flavor
import liqp.tag.LTag
import liqp.tag.Tags
import java.io.File
import java.util.function.Consumer

interface ParseSettingsSpec {
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

  fun configure(): MutableParseSettings
}

data class ParseSettings(override val tags: Tags = Liquify.provider.defaultTags,
                         override val filters: Filters = Liquify.provider.defaultFilters,
                         override val baseDir: File,
                         override val includesDir: File,
                         override val isStrictVariables: Boolean = false,
                         override val isStripSpacesAroundTags: Boolean = false,
                         override val isStripSingleLine: Boolean = false,
                         override val maxTemplateSize: Long? = null,
                         override val isKeepParseTree: Boolean = false,

                         override val cacheSettings: CacheSetup? = null) : ParseSettingsSpec {

  override fun configure(): MutableParseSettings {
    return MutableParseSettings(this)
  }

  fun withFilters(vararg filter: LFilter): ParseSettings {
    return this.copy(filters = this.filters + listOf(*filter))
  }

  fun withTags(vararg tags: LTag): ParseSettings {
    return this.copy(tags = this.tags + listOf(*tags))
  }
}

/**
 * This class is largely to support interop with java builders.  kotlin code should avoid this,
 * and just use the copy methods on {@link RenderSettings} directly
 */
data class MutableParseSettings(private var settings: ParseSettings) :
    ParseSettingsSpec by settings {

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

  fun addTags(vararg tags: LTag): MutableParseSettings {
    settings = settings.copy(tags = settings.tags + listOf(*tags))
    return this
  }

  fun addFilters(vararg filters: LFilter): MutableParseSettings {
    settings = settings.copy(filters = settings.filters + listOf(*filters))
    return this
  }

  fun includesDir(includesDir: File): MutableParseSettings {
    settings = settings.copy(includesDir = includesDir)
    return this
  }

  fun forJekyll():MutableParseSettings {
    settings = settings.copy(includesDir = File(Flavor.JEKYLL.includesDirName))
    return this
  }

  fun forLiquid():MutableParseSettings {
    settings = settings.copy(includesDir = File(Flavor.LIQUID.includesDirName))
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

  fun build(): ParseSettings = settings

  fun toParser(): LParser = Liquify.provider.createParser(this.build())
}

interface CacheSetup : Consumer<CacheBuilder<*, *>>

fun <K, V> CacheBuilder<K, V>.withSettings(setup: CacheSetup?): CacheBuilder<K, V> {
  setup?.accept(this)
  return this
}

