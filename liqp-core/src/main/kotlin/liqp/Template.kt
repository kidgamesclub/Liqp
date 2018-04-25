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
               private val engine: LiquidRenderer = LiquidRenderer.newInstance(parser.toRenderSettings())) : LTemplate {

  @JvmOverloads
  fun render(inputData: String, engine: LiquidRenderer = this.engine): String {
    return engine.render(this, inputData)
  }

  @JvmOverloads
  fun render(inputData: Any? = null, engine: LiquidRenderer = this.engine): String {
    return engine.render(this, inputData)
  }

  @JvmOverloads
  fun render(key: String, value: Any?, engine: LiquidRenderer = this.engine): String {
    return engine.render(this, key to value)
  }

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
