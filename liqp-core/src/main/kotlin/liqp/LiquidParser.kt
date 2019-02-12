package liqp

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import liqp.LiquidDefaults.defaultFilters
import liqp.LiquidDefaults.defaultTags
import liqp.config.LParseSettings
import liqp.config.MutableParseSettings
import liqp.config.ParseSettings
import liqp.config.RenderSettings
import liqp.config.withSettings
import liqp.exceptions.InvalidTemplateException
import liqp.filter.LFilter
import liqp.node.LTemplate
import liqp.parser.Flavor.LIQUID
import liqp.parser.v4.NodeVisitor
import liqp.tag.LTag
import liquid.parser.v4.LiquidLexer
import liquid.parser.v4.LiquidParser
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.tree.ParseTree
import java.io.File
import java.io.FileNotFoundException
import kotlin.text.Charsets.UTF_8

val defaultParseSettings = ParseSettings(
    defaultTags,
    defaultFilters,
    baseDir = File("./"),
    includesDir = LIQUID.includesDirName)

data class LiquidParser @JvmOverloads constructor(override val settings: ParseSettings = defaultParseSettings) :
    LParseSettings by settings,
    LParser {

  companion object {
    @JvmStatic
    val defaultInstance = LiquidParser()

    @JvmStatic
    fun newInstance(): liqp.LiquidParser {
      return LiquidParser()
    }

    @JvmStatic
    fun newBuilder(): MutableParseSettings {
      return MutableParseSettings(defaultParseSettings)
    }

    @JvmStatic
    fun build(configurer: MutableParseSettings.() -> Unit = {}): liqp.LiquidParser {
      return LiquidParser(newBuilder().apply(configurer).build())
    }
  }

  override fun toRenderSettings(): RenderSettings {
    return RenderSettings(this.settings)
  }

  override fun parse(template: String): LiquidTemplate {
    return cache.get(template)!!
  }

  override fun parseFile(file: File): LTemplate {
    if (!file.exists()) {
      throw FileNotFoundException(file.absolutePath)
    }
    return parse(file.readText(UTF_8))
  }

  private val cache: LoadingCache<String?, LiquidTemplate> = CacheBuilder.newBuilder()
      .withSettings(cacheSettings)
      .build(CacheLoader.from { template: String? ->
        internalCreateTemplate(template!!)
      })

  private fun internalCreateTemplate(template: String): LiquidTemplate {
    if (maxTemplateSize != null && template.length.toLong() > maxTemplateSize) {
      throw InvalidTemplateException("template exceeds $maxTemplateSize")
    }

    val lexer = createLexer(template)
    val parser = createParser(lexer)

    val tree = parser.parse()
    val visitor = NodeVisitor(tags, filters)
    val rootNode = visitor.visit(tree)
    val parseTree: ParseTree? = when (this.isKeepParseTree) {
      false -> null
      true -> tree
    }
    val renderer = LiquidRenderer(parser = this, settings = this.toRenderSettings())
    return LiquidTemplate(rootNode, parseTree, this, renderer)
  }

  fun createLexer(template: String): LiquidLexer {
    val lexer = LiquidLexer(CharStreams.fromString(template), isStripSpacesAroundTags, isStripSingleLine)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorHandler)

    return lexer
  }

  fun createParser(lexer: LiquidLexer): LiquidParser {
    val parser = LiquidParser(CommonTokenStream(lexer))
    parser.removeErrorListeners()
    parser.addErrorListener(errorHandler)

    return parser
  }

  fun withFilters(vararg filters: LFilter): liqp.LiquidParser {
    return this.copy(settings = this.settings.withFilters(*filters))
  }

  fun withTags(vararg tags: LTag): liqp.LiquidParser {
    return this.copy(settings = this.settings.withTags(*tags))
  }

  override fun toMutableSettings(): MutableParseSettings {
    return MutableParseSettings(settings)
  }
}

val errorHandler = object : BaseErrorListener() {
  override fun syntaxError(recognizer: Recognizer<*, *>?,
                           offendingSymbol: Any,
                           line: Int,
                           charPositionInLine: Int,
                           msg: String?,
                           e: RecognitionException) {
    throw InvalidTemplateException(String.format("parser error on line %s, index %s", line,
        charPositionInLine), e)
  }
}


