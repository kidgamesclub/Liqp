package liqp.exceptions

import liquid.parser.v4.LiquidParser
import org.antlr.runtime.EarlyExitException
import org.antlr.runtime.MismatchedTokenException
import org.antlr.runtime.NoViableAltException
import org.antlr.runtime.RecognitionException
import org.antlr.v4.runtime.ParserRuleContext

class LiquidException : RuntimeException {
  val line: Int
  val charPositionInLine: Int

  constructor(e: RecognitionException) : super(createMessage(e), e) {
    this.line = e.line
    this.charPositionInLine = e.charPositionInLine
  }

  constructor(message: String, ctx: ParserRuleContext) : super(message) {

    this.line = ctx.start.line
    this.charPositionInLine = ctx.start.charPositionInLine
  }

  companion object {

    private fun createMessage(e: RecognitionException): String {

      val inputLines = e.input.toString().split("\r?\n|\r".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val errorLine = inputLines[e.line - 1]

      val message = StringBuilder(String.format("\nError on line %s, column %s:\n", e.line, e.charPositionInLine))

      message.append(errorLine).append("\n")

      for (i in 0 until e.charPositionInLine) {
        message.append(" ")
      }

      message.append("^")

      if (e is MismatchedTokenException) {

        return String.format("%s\nmatched '%s' as token <%s>, expecting token <%s>",
            message, e.token.text, tokenName(e.unexpectedType), tokenName(e.expecting))
      }

      if (e is EarlyExitException) {

        return String.format("%s\nmissing character '%s' after position %s",
            message, e.c.toChar(), e.charPositionInLine)
      }

      return if (e is NoViableAltException) {

        String.format("%s\ncould not decide what path to take, at position %s, expecting one of: %s",
            message, e.charPositionInLine, e.grammarDecisionDescription)
      } else "$message\nAn unknown error occurred!"
    }

    private fun tokenName(type: Int): String {
      return if (type < 0) "<EOF>" else LiquidParser.VOCABULARY.getSymbolicName(type)
    }
  }
}
