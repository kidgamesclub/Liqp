package liqp.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.TemplateFactory;
import org.junit.Test;

public class ParseTest {

    /*
     * def test_error_with_css
     *   text = %| div { font-weight: bold; } |
     *   template = TemplateFactory.newBuilder().parse(text)
     *
     *   assert_equal text, template.render
     *   assert_equal [String], template.root.nodelist.collect {|i| i.class}
     * end
     */
    @Test
    public void error_with_cssTest() throws Exception {

        String text = " div { font-weight: bold; } ";

        assertThat(TemplateFactory.newBuilder().parse(text).render(), is(text));
    }

    /*
     * def test_raise_on_single_close_bracet
     *   assert_raise(SyntaxError) do
     *     TemplateFactory.newBuilder().parse("text {{method} oh nos!")
     *   end
     * end
     */
    @Test(expected=RuntimeException.class)
    public void raise_on_single_close_bracetTest() throws Exception {
        TemplateFactory.newBuilder().parse("text {{method} oh nos!");
    }

    /*
     * def test_raise_on_label_and_no_close_bracets
     *   assert_raise(SyntaxError) do
     *     TemplateFactory.newBuilder().parse("TEST {{ ")
     *   end
     * end
     */
    @Test(expected=RuntimeException.class)
    public void raise_on_label_and_no_close_bracetsTest() throws Exception {
        TemplateFactory.newBuilder().parse("TEST {{ ");
    }

    /*
     * def test_raise_on_label_and_no_close_bracets_percent
     *   assert_raise(SyntaxError) do
     *     TemplateFactory.newBuilder().parse("TEST {% ")
     *   end
     * end
     */
    @Test(expected=RuntimeException.class)
    public void raise_on_label_and_no_close_bracets_percentTest() throws Exception {
        TemplateFactory.newBuilder().parse("TEST {% ");
    }

    /*
     * def test_error_on_empty_filter
     *   assert_nothing_raised do
     *     TemplateFactory.newBuilder().parse("{{test |a|b|}}")
     *     TemplateFactory.newBuilder().parse("{{test}}")
     *     TemplateFactory.newBuilder().parse("{{|test|}}")
     *   end
     * end
     */
    @Test
    public void error_on_empty_filterTest() throws Exception {
        //TemplateFactory.newBuilder().parse("{{test |a|b|}}"); // TODO isn't allowed (yet?)
        TemplateFactory.newBuilder().parse("{{test}}");
        //TemplateFactory.newBuilder().parse("{{|test|}}"); // TODO isn't allowed (yet?)
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
        assertThat(TemplateFactory.newBuilder().parse("{% if " + markup + " %} YES {% endif %}").render(assigns), is(" YES "));
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

        //assertThat(TemplateFactory.newBuilder().parse("{% if true && false %} YES {% endif %}").render(), is(" YES ")); // TODO isn't allowed (yet?)

        //assertThat(TemplateFactory.newBuilder().parse("{% if true || false %} YES {% endif %}").render(), is(" YES ")); // TODO isn't allowed (yet?)
    }

    @Test
    public void keywords_as_identifier() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("var2:{{var2}} {%assign var2 = var.comment%} var2:{{var2}}")
                        .render(" { \"var\": { \"comment\": \"content\" } } "),
                is("var2:  var2:content"));

        assertThat(
                TemplateFactory.newBuilder().parse("var2:{{var2}} {%assign var2 = var.end%} var2:{{var2}}")
                        .render(" { \"var\": { \"end\": \"content\" } } "),
                is("var2:  var2:content"));
    }
}
