package liqp

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import liqp.exceptions.InvalidTemplateException
import liqp.filters.LFilter
import liqp.parser.v4.NodeVisitor
import liqp.tags.Tag
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
import java.util.function.Consumer
import kotlin.text.Charsets.UTF_8

data class LiquidParser
@JvmOverloads constructor(private val settings: ParseSettings = ParseSettings()) : ParseOperations, ParseSettingsSpec by settings {

  companion object {
    @JvmStatic
    val defaultInstance = LiquidParser()

    @JvmStatic
    fun newInstance(): liqp.LiquidParser {
      return LiquidParser()
    }

    @JvmStatic
    fun newBuilder(): MutableParseSettings {
      return MutableParseSettings()
    }

    @JvmStatic
    fun newBuilder(configure: MutableParseSettings.() -> Unit = {}): liqp.LiquidParser {
      val builder = MutableParseSettings()
      builder.configure()
      return LiquidParser(builder.settings)
    }
  }

  fun toRenderSettings(): RenderSettings {
    return RenderSettings(baseDir = baseDir, flavor = flavor)
  }

  override fun parse(template: String): Template {
    return cache.get(template)!!
  }

  override fun parseFile(file: File): Template {
    if (!file.exists()) {
      throw FileNotFoundException(file.absolutePath)
    }
    return parse(file.readText(UTF_8))
  }

  private val cache: LoadingCache<String?, Template> = CacheBuilder.newBuilder()
      .withSettings(cacheSettings)
      .build(CacheLoader.from { template: String? ->
        internalCreateTemplate(template!!)
      })

  private fun internalCreateTemplate(template: String): Template {
    if (maxTemplateSize != null && template.length.toLong() > maxTemplateSize) {
      throw InvalidTemplateException("template exceeds $maxTemplateSize")
    }

    val lexer = createLexer(template)
    val parser = createParser(lexer)

    val tree = parser.parse()
    val visitor = NodeVisitor(tags, filters, flavor)
    val rootNode = visitor.visit(tree)
    val parseTree: ParseTree? = when (this.isKeepParseTree) {
      false -> null
      true -> tree
    }
    return Template(rootNode, parseTree, this)
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

  fun withFilters(vararg filters: LFilter): liqp.LiquidParser {
    return toMutableParseSettings().addFilters(*filters).toParser()
  }

  fun withTags(vararg tags: Tag): liqp.LiquidParser {
    return toMutableParseSettings().addTags(*tags).toParser()
  }

  override fun toMutableParseSettings(): MutableParseSettings {
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

interface CacheSetup : Consumer<CacheBuilder<*, *>>

fun <K, V> CacheBuilder<K, V>.withSettings(setup: CacheSetup?): CacheBuilder<K, V> {
  setup?.accept(this)
  return this
}

