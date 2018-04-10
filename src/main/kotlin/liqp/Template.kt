package liqp

import liqp.nodes.LNode
import org.antlr.v4.runtime.tree.ParseTree

/**
 * This class holds the state of a parsed template
 */
class Template(val rootNode: LNode, private val parseTree: ParseTree?, private val engine: TemplateEngine = TemplateEngine.newInstance()) {

  @JvmOverloads
  fun render(inputData: String, engine: TemplateEngine = this.engine): String {
    return engine.render(this, inputData)
  }

  @JvmOverloads
  fun render(inputData: Any? = null, engine: TemplateEngine = this.engine): String {
    return engine.render(this, inputData)
  }

  @JvmOverloads
  fun render(key: String, value: Any?, engine: TemplateEngine = this.engine): String {
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
      null-> "No debug info available.  Set isKeepParseTree in TemplateFactory"
      else-> ParseTreeRenderer.renderParseTree(this.parseTree)
    }
  }

  override fun toString(): String {
    return toStringTree()
  }
}
