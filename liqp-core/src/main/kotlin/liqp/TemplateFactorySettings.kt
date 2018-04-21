package liqp

import liqp.filters.Filter
import liqp.filters.Filters
import liqp.filters.LFilter
import liqp.parser.Flavor
import liqp.tags.Tag
import liqp.tags.Tags
import java.io.File
import java.io.FileNotFoundException

data class TemplateFactorySettings(var flavor: Flavor = Flavor.LIQUID,
                                   var isStrictVariables: Boolean = false,
                                   var isStripSpacesAroundTags: Boolean = false,
                                   var isStripSingleLine: Boolean = false,
                                   var maxTemplateSize: Long? = null,
                                   var isKeepParseTree: Boolean = false,
                                   var tags: Tags = Tags.getDefaultTags(),
                                   var filters: Filters = Filters.getDefaultFilters(),
                                   var cacheSettings: CacheSetup? = null) {

  fun flavor(flavor: Flavor): TemplateFactorySettings {
    this.flavor = flavor
    return this
  }

  fun strictVariables(strictVariables: Boolean): TemplateFactorySettings {
    isStrictVariables = strictVariables
    return this
  }

  fun stripSpacesAroundTags(stripSpacesAroundTags: Boolean): TemplateFactorySettings {
    isStripSpacesAroundTags = stripSpacesAroundTags
    return this
  }

  fun stripSingleLine(stripSingleLine: Boolean): TemplateFactorySettings {
    isStripSingleLine = stripSingleLine
    return this
  }

  fun maxTemplateSize(maxTemplateSize: Long?): TemplateFactorySettings {
    this.maxTemplateSize = maxTemplateSize
    return this
  }

  fun withTags(vararg tags: Tag):TemplateFactorySettings {
    this.tags = this.tags.withTags(*tags)
    return this
  }

  fun withFilters(vararg filters: LFilter):TemplateFactorySettings {
    this.filters = this.filters.withFilters(*filters)
    return this
  }

  fun tags(tags: Tags): TemplateFactorySettings {
    this.tags = tags
    return this
  }

  fun filters(filters: Filters): TemplateFactorySettings {
    this.filters = filters
    return this
  }

  fun cacheSettings(cacheSettings: CacheSetup): TemplateFactorySettings {
    this.cacheSettings = cacheSettings
    return this
  }

  fun parse(template: String): Template {
    return TemplateFactory(this).parse(template)
  }

  fun build(): TemplateFactory {
    return TemplateFactory(this)
  }

  fun parseFile(file: File): Template {
    if (!file.exists()) {
      throw FileNotFoundException(file.absolutePath)
    }
    return TemplateFactory(this).parseFile(file)
  }
}
