package liqp.parser;

import static liqp.AssertsKt.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidParser;
import org.junit.Test;

public class ParseTest {

  /*
   * def test_error_with_css
   *   text = %| div { font-weight: bold; } |
   *   template = TemplateFactory.newInstance().parse(text)
   *
   *   assert_equal text, template.render
   *   assert_equal [String], template.root.nodelist.collect {|i| i.class}
   * end
   */
  @Test
  public void error_with_cssTest() throws Exception {

    String text = " div { font-weight: bold; } ";

    assertThat(createTestParser().parse(text).render(), is(text));
  }

  /*
   * def test_raise_on_single_close_bracet
   *   assert_raise(SyntaxError) do
   *     TemplateFactory.newInstance().parse("text {{method} oh nos!")
   *   end
   * end
   */
  @Test(expected = RuntimeException.class)
  public void raise_on_single_close_bracetTest() throws Exception {
    createTestParser().parse("text {{method} oh nos!");
  }

  /*
   * def test_raise_on_label_and_no_close_bracets
   *   assert_raise(SyntaxError) do
   *     TemplateFactory.newInstance().parse("TEST {{ ")
   *   end
   * end
   */
  @Test(expected = RuntimeException.class)
  public void raise_on_label_and_no_close_bracetsTest() throws Exception {
    createTestParser().parse("TEST {{ ");
  }

  /*
   * def test_raise_on_label_and_no_close_bracets_percent
   *   assert_raise(SyntaxError) do
   *     TemplateFactory.newInstance().parse("TEST {% ")
   *   end
   * end
   */
  @Test(expected = RuntimeException.class)
  public void raise_on_label_and_no_close_bracets_percentTest() throws Exception {
    createTestParser().parse("TEST {% ");
  }

  /*
   * def test_error_on_empty_filter
   *   assert_nothing_raised do
   *     TemplateFactory.newInstance().parse("{{test |a|b|}}")
   *     TemplateFactory.newInstance().parse("{{test}}")
   *     TemplateFactory.newInstance().parse("{{|test|}}")
   *   end
   * end
   */
  @Test
  public void error_on_empty_filterTest() throws Exception {
    //TemplateFactory.newInstance().parse("{{test |a|b|}}"); // TODO isn't allowed (yet?)
    createTestParser().parse("{{test}}");
    //TemplateFactory.newInstance().parse("{{|test|}}"); // TODO isn't allowed (yet?)
  }

  /*
   * def test_meaningless_parens
   *   assigns = {'b' => 'bar', 'c' => 'baz'}
   *   markup = "a == 'foo' or (b == 'bar' and c == 'baz') or false"
   *   assert_template_result(' YES ',"{% if #{markup} %} YES {% endif %}", assigns)
   * end
   */
  @Test
  public void meaningless_parensTest() throws Exception {

    String assigns = "{\"b\" : \"bar\", \"c\" : \"baz\"}";
    String markup = "a == 'foo' or (b == 'bar' and c == 'baz') or false";
    assertThat(createTestParser().parse("{% if " + markup + " %} YES {% endif %}").renderJson(assigns), is(" " +
          "YES "));
  }

  /*
   * def test_unexpected_characters_silently_eat_logic
   *   markup = "true && false"
   *   assert_template_result(' YES ',"{% if #{markup} %} YES {% endif %}")
   *   markup = "false || true"
   *   assert_template_result('',"{% if #{markup} %} YES {% endif %}")
   * end
   */
  @Test
  public void unexpected_characters_silently_eat_logicTest() throws Exception {

    //assertThat(TemplateFactory.newInstance().parse("{% if true && false %} YES {% endif %}").render(), is(" YES "))
      // ; // TODO isn't allowed (yet?)

    //assertThat(TemplateFactory.newInstance().parse("{% if true || false %} YES {% endif %}").render(), is(" YES "))
      // ; // TODO isn't allowed (yet?)
  }

  @Test
  public void keywords_as_identifier() throws Exception {

    assertThat(
          createTestParser().parse("var2:{{var2}} {%assign var2 = var.comment%} var2:{{var2}}")
                .renderJson(" { \"var\": { \"comment\": \"content\" } } "),
          is("var2:  var2:content"));

    assertThat(
          createTestParser().parse("var2:{{var2}} {%assign var2 = var.end%} var2:{{var2}}")
                .renderJson(" { \"var\": { \"end\": \"content\" } } "),
          is("var2:  var2:content"));
  }
}
