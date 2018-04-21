package liqp.nodes;


import liqp.LiquidParser;
import liqp.tags.Tag;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class BlockNodeTest {

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
  public void customTagTest() throws RecognitionException {

    Tag customTag = new Tag("testtag") {
      @Override
      public Object render(RenderContext context, LNode... nodes) {
        return null;
      }
    };

    LiquidParser.newInstance().withTags(customTag).parse("{% testtag %} {% endtesttag %}").render();
  }
}
