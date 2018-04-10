package liqp

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import liqp.exceptions.InvalidTemplateException
import liqp.filters.Filter
import liqp.filters.Filters
import liqp.parser.Flavor
import liqp.parser.ParseTreeConverter
import liqp.tags.Tags
import liquid.parser.v4.LiquidLexer
import liquid.parser.v4.LiquidParser
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import java.io.File
import java.util.function.Consumer
import kotlin.text.Charsets.UTF_8

data class TemplateFactory(val flavor: Flavor = Flavor.LIQUID,
                           val isStrictVariables: Boolean = false,
                           val isStripSpacesAroundTags: Boolean = false,
                           val isStripSingleLine: Boolean = false,
                           val maxTemplateSize: Long? = null,
                           val tags: Tags = Tags.getDefaultTags(),
                           val filters: Filters = Filters.getDefaultFilters(),
                           val cacheSettings: CacheSetup? = null) {

  constructor(settings:TemplateFactorySettings):this(settings.flavor,
      settings.isStrictVariables, settings.isStripSpacesAroundTags, settings.isStripSingleLine,
      settings.maxTemplateSize, settings.tags, settings.filters, settings.cacheSettings)

  companion object {
    @JvmStatic
    val defaultInstance = TemplateFactory()

    @JvmStatic
    fun newInstance(): TemplateFactory {
      return TemplateFactory()
    }

    @JvmStatic
    fun newBuilder(): TemplateFactorySettings {
      return TemplateFactorySettings()
    }
  }

  fun parse(template: String): Template {
    return cache.get(template)!!
  }

  fun parseFile(file: File): Template {
    return parse(file.readText(UTF_8))
  }

  fun withFilter(filter: Filter): TemplateFactory {
    return this.copy(filters = filters.withFilters(filter))
  }

  fun toBuilder(): TemplateFactorySettings {
    return TemplateFactorySettings(flavor, isStrictVariables, isStripSpacesAroundTags, isStripSingleLine,
        maxTemplateSize, tags, filters, cacheSettings)
  }

  private val cache: LoadingCache<String, Template> = (CacheBuilder.newBuilder() as CacheBuilder<String, Template>)
      .withSettings(cacheSettings)
      .build(CacheLoader.from({ template ->
        internalCreateTemplate(template!!)
      }))

  private fun internalCreateTemplate(template: String): Template {
    if (maxTemplateSize != null && template.length.toLong() > maxTemplateSize) {
      throw InvalidTemplateException("template exceeds $maxTemplateSize")
    }

    val lexer = createLexer(template)
    val parser = createParser(lexer)

    val rootNode = ParseTreeConverter().converTreeToLNode(parser, tags, filters, flavor,
        isStrictVariables)
    return Template(rootNode)
  }

  private fun createLexer(template: String): LiquidLexer {
    val lexer = LiquidLexer(CharStreams.fromString(template), isStripSpacesAroundTags, isStripSingleLine)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorHandler)

    return lexer
  }

  private fun createParser(lexer: LiquidLexer): LiquidParser {
    val parser = LiquidParser(CommonTokenStream(lexer))
    parser.removeErrorListeners()
    parser.addErrorListener(errorHandler)

    return parser
  }
}

val errorHandler = object : BaseErrorListener() {
  override fun syntaxError(recognizer: Recognizer<*, *>?,
                           offendingSymbol: Any?,
                           line: Int,
                           charPositionInLine: Int,
                           msg: String?,
                           e: RecognitionException?) {
    throw InvalidTemplateException(String.format("parser error on line %s, index %s", line,
        charPositionInLine), e)
  }
}

interface CacheSetup : Consumer<CacheBuilder<*, *>>

inline fun <K, V> CacheBuilder<K, V>.withSettings(setup: CacheSetup?): CacheBuilder<K, V> {
  setup?.accept(this)
  return this
}

