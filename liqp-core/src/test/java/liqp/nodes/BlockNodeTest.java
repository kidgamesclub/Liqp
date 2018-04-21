package liqp.nodes;


import liqp.TemplateFactory;
import liqp.tags.Tag;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class BlockNodeTest {

  /*
   * def test_with_custom_tag
   *   Liquid::Template.register_tag("testtag", Block)
   *
   *   assert_nothing_thrown do
   *     template = Liquid::TemplateFactory.newBuilder().parse( "{% testtag %} {% endtesttag %}")
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

    TemplateFactory.newBuilder().withTags(customTag).parse("{% testtag %} {% endtesttag %}").render();
  }
}
