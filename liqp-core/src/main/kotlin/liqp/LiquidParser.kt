package liqp

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import liqp.config.LParseSettings
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

data class LiquidParser constructor(override val parseSettings: LParseSettings) : LParser {

  override fun toRenderSettings(): RenderSettings {
    return RenderSettings(this.parseSettings)
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
      .withSettings(parseSettings.cacheSettings)
      .build(CacheLoader.from { template: String? ->
        internalCreateTemplate(template!!)
      })

  private fun internalCreateTemplate(template: String): LiquidTemplate {
    if (parseSettings.maxTemplateSize != null && template.length.toLong() > parseSettings.maxTemplateSize!!) {
      throw InvalidTemplateException("template exceeds ${parseSettings.maxTemplateSize}")
    }

    val lexer = createLexer(template)
    val parser = createParser(lexer)

    val tree = parser.parse()
    val visitor = NodeVisitor(parseSettings.tags, parseSettings.filters)
    val rootNode = visitor.visit(tree)
    val parseTree: ParseTree? = when (parseSettings.isKeepParseTree) {
      false -> null
      true -> tree
    }
    val renderer = LiquidRenderer(parser = this, renderSettings = this.toRenderSettings())
    return LiquidTemplate(rootNode, parseTree, this, renderer)
  }

  fun createLexer(template: String): LiquidLexer {
    val lexer = LiquidLexer(CharStreams.fromString(template), parseSettings.isStripSpacesAroundTags, parseSettings.isStripSingleLine)
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
}


