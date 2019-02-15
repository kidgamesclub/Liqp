package liqp

import liqp.config.RenderSettings
import liqp.node.LNode
import liqp.node.LTemplate
import org.antlr.v4.runtime.tree.ParseTree

/**
 * This class holds the state of a parsed template
 */
data class LiquidTemplate(override val rootNode: LNode,
                          private val parseTree: ParseTree?,
                          private val parser: LParser,
                          private val providedRenderer: LRenderer?) : LTemplate {

  override val renderer: LRenderer = providedRenderer ?: Liquify.provider.createRenderer(parser,
      RenderSettings(parser.parseSettings))

  /**
   * Returns a string representation of the parse tree of the parsed
   * input source.
   *
   * @return a string representation of the parse tree of the parsed
   * input source.
   */
  fun toStringTree(): String {
    return when (parseTree) {
      null -> "No debug info available.  Set isKeepParseTree in TemplateFactory"
      else -> ParseTreeRenderer.renderParseTree(this.parseTree)
    }
  }

  override fun toString(): String {
    return toStringTree()
  }
}
