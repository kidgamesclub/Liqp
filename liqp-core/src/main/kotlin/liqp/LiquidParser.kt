package liqp

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import liqp.config.MutableParseSettings
import liqp.config.ParseSettings
import liqp.config.ParseSettingsSpec
import liqp.config.RenderSettings
import liqp.config.withSettings
import liqp.exceptions.InvalidTemplateException
import liqp.filter.LFilter
import liqp.node.LTemplate
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
    includesDir = File("./includes"))

data class LiquidParser
@JvmOverloads constructor(val settings: ParseSettings = defaultParseSettings) :
    ParseSettingsSpec by settings,
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
    fun newBuilder(configurer: MutableParseSettings.() -> Unit = {}): liqp.LiquidParser {
      val settings = newBuilder()
      settings.configurer()
      return LiquidParser(settings.build())
    }
  }

  override fun toRenderSettings(): RenderSettings {
    return RenderSettings(baseDir = baseDir, includesDir = includesDir)
  }

  override fun parse(template: String): LTemplate {
    return cache.get(template)!!
  }

  override fun parseFile(file: File): LTemplate {
    if (!file.exists()) {
      throw FileNotFoundException(file.absolutePath)
    }
    return parse(file.readText(UTF_8))
  }

  private val cache: LoadingCache<String?, LTemplate> = CacheBuilder.newBuilder()
      .withSettings(cacheSettings)
      .build(CacheLoader.from { template: String? ->
        internalCreateTemplate(template!!)
      })

  private fun internalCreateTemplate(template: String): LTemplate {
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
    return Template(rootNode, parseTree, this)
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

  override fun configure(): MutableParseSettings {
    return MutableParseSettings(settings)
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


