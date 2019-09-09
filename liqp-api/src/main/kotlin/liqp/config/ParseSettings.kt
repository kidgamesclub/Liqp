package liqp.config

import com.google.common.cache.CacheBuilder
import liqp.LParser
import liqp.Liquify
import liqp.filter.Filters
import liqp.filter.LFilter
import liqp.parser.Flavor.JEKYLL
import liqp.parser.Flavor.LIQUID
import liqp.tag.LTag
import liqp.tag.Tags
import java.io.File
import java.util.function.Consumer

interface LParseSettings {
  val tags: Tags
  val filters: Filters
  val baseDir: File
  val includesDir: String
  val isStrictVariables: Boolean
  val isStrictIncludes: Boolean
  val isStripSpacesAroundTags: Boolean
  val isStripSingleLine: Boolean
  val maxTemplateSize: Long?
  val isKeepParseTree: Boolean
  val cacheSettings: CacheSetup?

  fun withFilters(vararg filters: LFilter): LParseSettings
  fun withTags(vararg tags: LTag): LParseSettings

  fun toParser(): LParser
  fun toMutableSettings(): MutableParseSettings
  fun reconfigure(block:MutableParseSettings.()->Unit):LParseSettings = toMutableSettings().build(block)
}

data class ParseSettings(override val tags: Tags = Liquify.provider.defaultTags,
                         override val filters: Filters = Liquify.provider.defaultFilters,
                         override val baseDir: File,
                         override val includesDir: String,
                         override val isStrictVariables: Boolean = false,
                         override val isStrictIncludes: Boolean = false,
                         override val isStripSpacesAroundTags: Boolean = false,
                         override val isStripSingleLine: Boolean = false,
                         override val maxTemplateSize: Long? = null,
                         override val isKeepParseTree: Boolean = false,
                         override val cacheSettings: CacheSetup? = null) : LParseSettings {

  constructor(settings: MutableParseSettings) : this(settings.tags, settings.filters, settings.baseDir, settings.includesDir,
      settings.isStrictVariables, settings.isStrictIncludes, settings.isStripSpacesAroundTags, settings.isStripSingleLine,
      settings.maxTemplateSize, settings.isKeepParseTree, settings.cacheSettings)

  override fun toMutableSettings(): MutableParseSettings {
    return MutableParseSettings(this)
  }

  override fun withFilters(vararg filters: LFilter): ParseSettings {
    return this.copy(filters = this.filters + listOf(*filter))
  }

  override fun withTags(vararg tags: LTag): ParseSettings {
    return this.copy(tags = this.tags + listOf(*tags))
  }

  override fun toParser(): LParser = Liquify.provider.createParser(this)
}

/**
 * This class is largely to support interop with java builders.  kotlin code should avoid this,
 * and just use the copy methods on {@link RenderSettings} directly
 */
data class MutableParseSettings(var tags: Tags = Liquify.provider.defaultTags,
                                var filters: Filters = Liquify.provider.defaultFilters,
                                var baseDir: File,
                                var includesDir: String,
                                var isStrictVariables: Boolean = false,
                                var isStrictIncludes: Boolean,
                                var isStripSpacesAroundTags: Boolean = false,
                                var isStripSingleLine: Boolean = false,
                                var maxTemplateSize: Long? = null,
                                var isKeepParseTree: Boolean = false,
                                var cacheSettings: CacheSetup? = null) {

  constructor(settings: ParseSettings) : this(settings.tags, settings.filters, settings.baseDir, settings.includesDir,
      settings.isStrictVariables, settings.isStrictIncludes, settings.isStripSpacesAroundTags, settings.isStripSingleLine,
      settings.maxTemplateSize, settings.isKeepParseTree, settings.cacheSettings)

  @JvmOverloads fun build(block: MutableParseSettings.() -> Unit = {}): ParseSettings = ParseSettings(this.apply(block))

  fun baseDir(baseDir: File): MutableParseSettings {
    this.baseDir = baseDir
    return this
  }

  fun strictVariables(isStrictVariables: Boolean): MutableParseSettings {
    this.isStrictVariables = isStrictVariables
    return this
  }

  fun strictIncludes(isStrictIncludes: Boolean): MutableParseSettings {
    this.isStrictIncludes = isStrictIncludes
    return this
  }

  fun stripSpacesAroundTags(stripSpacesAroundTags: Boolean): MutableParseSettings {
    this.isStripSpacesAroundTags = stripSpacesAroundTags
    return this
  }

  fun stripSingleLine(stripSingleLine: Boolean): MutableParseSettings {
    this.isStripSingleLine = stripSingleLine
    return this
  }

  fun maxTemplateSize(maxTemplateSize: Long?): MutableParseSettings {
    this.maxTemplateSize = maxTemplateSize
    return this
  }

  fun addTags(vararg tags: LTag): MutableParseSettings {
    this.tags = this.tags + tags.toList()
    return this
  }

  fun addFilters(vararg filters: LFilter): MutableParseSettings {
    this.filters = this.filters + filters.toList()
    return this
  }

  fun includesDir(includesDir: String): MutableParseSettings {
    this.includesDir = includesDir
    return this
  }

  fun forJekyll(): MutableParseSettings {
    this.includesDir = JEKYLL.includesDirName
    return this
  }

  fun forLiquid(): MutableParseSettings {
    this.includesDir = LIQUID.includesDirName
    return this
  }

  fun tags(tags: Tags): MutableParseSettings {
    this.tags = tags
    return this
  }

  fun filters(filters: Filters): MutableParseSettings {
    this.filters = filters
    return this
  }

  fun keepParseTree(isKeepParseTree: Boolean): MutableParseSettings {
    this.isKeepParseTree = isKeepParseTree
    return this
  }

  fun cacheSettings(cacheSettings: CacheSetup): MutableParseSettings {
    this.cacheSettings = cacheSettings
    return this
  }


}

interface CacheSetup : Consumer<CacheBuilder<*, *>>

fun <K, V> CacheBuilder<K, V>.withSettings(setup: CacheSetup?): CacheBuilder<K, V> {
  setup?.accept(this)
  return this
}

