package liqp.nodes

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidParser
import liqp.createTestParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class OutputNodeTest(val template: String, val expect: String) {
  @Test
  fun run() {

    val template = createTestParser{}.parse(template)
    val rendered = template.renderJson("{\"X\" : \"mu\"}")

    assert(rendered).isEqualTo(expect)
  }

  companion object {
    @JvmStatic @Parameterized.Parameters
    fun params() = arrayOf(arrayOf("{{ X }}", "mu"), arrayOf("{{ 'a.b.c' | split:'.' | first | upcase }}", "A"))
  }
}

@RunWith(Parameterized::class)
class OutputNodeKeywordTest(val keyword:String) {
  @Test
  fun testKeywordsAsVariables() {

    val test = "{{$keyword}}"
    val expected = keyword + "_" + Integer.toString(keyword.length)
    val json = "{\"$keyword\" : \"$expected\" }"
    val template = createTestParser {  }.parse(test)
    val rendered = template.renderJson(json)

    assert(rendered).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{0}")
    fun keywordParams(): Array<String> {
      return arrayOf("capture", "endcapture", "comment", "endcomment", "raw", "endraw", "if", "elsif",
          "endif", "unless", "endunless", "else", "contains", "case", "endcase", "when", "cycle",
          "for", "endfor", "in", "and", "or", "tablerow", "endtablerow", "assign", "include",
          "with", "end", "break", "continue")
    }
  }
}

@RunWith(Parameterized::class)
class OutputNodeBadKeywordTest(val keyword: String, val expected: String) {

  @Test fun badKeywordAsVariableTest() {
    val test = "{{$keyword}}"
    val json = "{\"$keyword\" : \"bad\" }"
    val template = createTestParser { }.parse(test)
    val rendered = template.renderJson(json)

    assert(rendered).isEqualTo(expected)
  }

  companion object {

    @JvmStatic @Parameterized.Parameters
    fun badKeywordVariables(): Array<Array<String>> {
      return arrayOf(arrayOf("true", "true"), arrayOf("false", "false"), arrayOf("nil", ""), arrayOf("null", "")) // {"empty", "Object"},
    }
  }
}
