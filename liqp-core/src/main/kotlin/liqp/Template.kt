package liqp

import liqp.node.LNode
import liqp.node.LTemplate
import org.antlr.v4.runtime.tree.ParseTree

/**
 * This class holds the state of a parsed template
 */
class Template(override val rootNode: LNode,
               private val parseTree: ParseTree?,
               private val parser: LiquidParser,
               override val renderer: LiquidRenderer = LiquidRenderer.newInstance(parser.toRenderSettings())) : LTemplate {

  override fun render(inputData: Any?): String = renderer.render(this, inputData)
  override fun render(key: String, value: Any?): String = renderer.render(this, key to value)
  override fun render(pair: Pair<String, Any?>): String = renderer.render(this, pair)
  override fun render(): String = renderer.render(this)

  fun render(key: String, value: Any?, engine: LiquidRenderer = this.renderer): String =
      engine.render(this, key to value)

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
