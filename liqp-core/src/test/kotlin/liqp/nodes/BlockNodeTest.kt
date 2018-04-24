package liqp.nodes

import liqp.LiquidParser
import liqp.rendering
import liqp.tags.Tag
import org.antlr.runtime.RecognitionException
import org.junit.Test

class BlockNodeTest {

  /*
   * def test_with_custom_tag
   *   Liquid::Template.register_tag("testtag", Block)
   *
   *   assert_nothing_thrown do
   *     template = Liquid::TemplateFactory.newInstance().parse( "{% testtag %} {% endtesttag %}")
   *   end
   * end
   */
  @Test
  @Throws(RecognitionException::class)
  fun customTagTest() {

    val customTag = object : Tag("testtag") {
      override fun render(context: RenderContext, vararg nodes: LNode): Any? {
        return null
      }
    }

    LiquidParser.newInstance().withTags(customTag).parse("{% testtag %} {% endtesttag %}").render()
  }

  @Test
  fun testRenderNonString() {
    LiquidParser.newInstance().parse("{{0}}")
        .rendering { }
        .isNotError()
        .isEqualTo(0L)
  }

  @Test
  fun testRenderNonStringPadding() {
    LiquidParser.newInstance().parse(" {{0}}")
        .rendering { }
        .isNotError()
        .isEqualTo(" 0")
  }

  @Test
  fun testRenderNonStringNested() {
    LiquidParser.newInstance().parse("{%if true%}{{ 0 }}{%else%}{{1}}{%endif%}")
        .rendering { }
        .isNotError()
        .isEqualTo(0L)
  }

  @Test
  fun testRenderNonStringNested1() {
    LiquidParser.newInstance().parse("{%if true%}{{ 1 }}{%else%} {{1}} {%endif%}")
        .rendering { }
        .isNotError()
        .isEqualTo(1L)
  }
}
