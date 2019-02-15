package liqp

import liqp.LiquidDefaults.defaultFilters
import liqp.LiquidDefaults.defaultTags

import liqp.node.LNode
import liqp.parser.v4.NodeVisitor
import liquid.parser.v4.LiquidLexer
import liquid.parser.v4.LiquidParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.apache.commons.lang3.exception.ExceptionUtils
import org.assertj.core.util.Throwables

object TestUtils {

  /**
   * Parses the input `source` and invokes the `rule` and returns this LNode.
   *
   * @param source
   * the input source to be parsed.
   * @param rule
   * the rule name (method name) to be invoked
   * @return
   * @throws Exception
   */
  @JvmStatic fun getNode(source: String): LNode {
    val lexer = LiquidLexer(CharStreams.fromString("{{ $source }}"))
    val parser = LiquidParser(CommonTokenStream(lexer))

    val root = parser.output()
    val visitor = NodeVisitor(defaultTags, defaultFilters)

    return visitor.visitOutput(root)
  }

  @JvmStatic fun getExceptionRootCause(e: Throwable): Throwable {
    return ExceptionUtils.getRootCause(e)
  }
} // no need to instantiate this class
