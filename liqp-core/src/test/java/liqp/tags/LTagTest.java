package liqp.tags;

import static liqp.AssertsKt.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidParser;
import liqp.context.LContext;
import liqp.node.LNode;
import liqp.node.LTemplate;
import liqp.tag.LTag;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class LTagTest {

  @Test
  public void testCustomTag() throws RecognitionException {

    final LTag custom = new LTag("twice") {
      @Override
      public Object render(LContext context, LNode... nodes) {
        Double number = context.asNumber(nodes[0].render(context)).doubleValue();
        return number * 2;
      }
    };

    LTemplate template = createParseSettings().addTags(custom).build().toParser().parse("{% twice 10 %}");
    String rendered = template.render();
    assertThat(rendered, is("20.0"));
  }

  @Test
  public void testCustomTagBlock() throws RecognitionException {

    final LTag custom = new LTag("twice") {
      @Override
      public Object render(LContext context, LNode... nodes) {
        LNode blockNode = nodes[nodes.length - 1];
        String blockValue = context.asString(blockNode.render(context));
        return blockValue + " " + blockValue;
      }
    };

    LTemplate template = createParseSettings()
          .addTags(custom)
          .build()
          .toParser()
          .parse("{% twice %}abc{% endtwice %}");
    String rendered = template.render();

    assertThat(rendered, is("abc abc"));
  }

  @Test
  public void breakTest() throws RecognitionException {

    final String context = "{\"array\":[11,22,33,44,55]}";

    final String markup = "{% for item in array %}" +
          "{% if item > 35 %}{% break %}{% endif %}" +
          "{{ item }}" +
          "{% endfor %}";

    assertThat(createTestParser().parse(markup).renderJson(context), is("112233"));
  }

  /*
   * def test_break_with_no_block
   *   assigns = {'i' => 1}
   *   markup = '{% break %}'
   *   expected = ''
   *
   *   assert_template_result(expected, markup, assigns)
   * end
   */
  @Test
  public void breakWithNoBlockTest() throws RecognitionException {

    assertThat(createTestParser().parse("{% break %}").render(), is(""));
  }

  @Test
  public void continueTest() throws RecognitionException {

    final String context = "{\"array\":[11,22,33,44,55]}";

    final String markup = "{% for item in array %}" +
          "{% if item < 35 %}{% continue %}{% endif %}" +
          "{{ item }}" +
          "{% endfor %}";

    assertThat(createTestParser().parse(markup).renderJson(context), is("4455"));
  }

  /*
   * def test_continue_with_no_block
   *   assigns = {'i' => 1}
   *   markup = '{% continue %}'
   *   expected = ''
   *
   *   assert_template_result(expected, markup, assigns)
   * end
   */
  @Test
  public void continueWithNoBlockTest() throws RecognitionException {

    assertThat(createTestParser().parse("{% continue %}").render(), is(""));
  }

  /*
   * def test_no_transform
   *   assert_template_result('this text should come out of the template without change...',
   *                          'this text should come out of the template without change...')
   *
   *   assert_template_result('blah','blah')
   *   assert_template_result('<blah>','<blah>')
   *   assert_template_result('|,.:','|,.:')
   *   assert_template_result('','')
   *
   *   text = %|this shouldnt see any transformation either but has multiple lines
   *             as you can clearly see here ...|
   *   assert_template_result(text,text)
   * end
   */
  @Test
  public void no_transformTest() {

    assertThat(createTestParser().parse("this text should come out of the template without change...").render(),
          is("this text should come out of the template without change..."));

    assertThat(createTestParser().parse("blah").render(), is("blah"));
    assertThat(createTestParser().parse("<blah>").render(), is("<blah>"));
    assertThat(createTestParser().parse("|,.:").render(), is("|,.:"));
    assertThat(createTestParser().parse("").render(), is(""));

    String text = "this shouldnt see any transformation either but has multiple lines\n as you can clearly see here " +
          "...";
    assertThat(createTestParser().parse(text).render(), is(text));
  }
}
