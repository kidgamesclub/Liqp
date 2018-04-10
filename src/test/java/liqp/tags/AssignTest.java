package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class AssignTest {

    @Test
    public void renderTest() throws RecognitionException {

        String[][] tests = {
                {"{% assign name = 'freestyle' %}{{ name }}", "freestyle"},
                {"{% assign age = 42 %}{{ age }}", "42"},
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }

        String json = "{\"values\":[\"A\", [\"B1\", \"B2\"], \"C\"]}";

        assertThat(TemplateFactory.newBuilder().parse("{% assign foo = values %}.{{ foo[1][1] }}.").render(json), is(".B2."));

        json = "{\"values\":[\"A\", {\"bar\":{\"xyz\":[\"B1\", \"ok\"]}}, \"C\"]}";

        assertThat(TemplateFactory.newBuilder().parse("{% assign foo = values %}.{{ foo[1].bar.xyz[1] }}.").render(json), is(".ok."));
    }

    /*
     * def test_assigned_variable
     *   assert_template_result('.foo.',
     *                          '{% assign foo = values %}.{{ foo[0] }}.',
     *                          'values' => %w{foo bar baz})
     *
     *   assert_template_result('.bar.',
     *                          '{% assign foo = values %}.{{ foo[1] }}.',
     *                          'values' => %w{foo bar baz})
     * end
     *
     * def test_assign_with_filter
     *   assert_template_result('.bar.',
     *                          '{% assign foo = values | split: "," %}.{{ foo[1] }}.',
     *                          'values' => "foo,bar,baz")
     * end
     */
    @Test
    public void applyOriginalTest() {

        final String[] values = {"foo", "bar", "baz"};

        assertThat(TemplateFactory.newBuilder().parse("{% assign foo = values %}.{{ foo[0] }}.").render("values", values), is(".foo."));
        assertThat(TemplateFactory.newBuilder().parse("{% assign foo = values %}.{{ foo[1] }}.").render("values", values), is(".bar."));

        assertThat(TemplateFactory.newBuilder().parse("{% assign foo = values | split: \",\" %}.{{ foo[1] }}.").render("values", "foo,bar,baz"), is(".bar."));
    }

    /*
     * def test_hyphenated_variable
     *
     *   @context['oh-my'] = 'godz'
     *   assert_equal 'godz', @context['oh-my']
     *
     * end
     */
    @Test
    public void hyphenatedVariableTest() throws Exception {

        assertThat(TemplateFactory.newBuilder().parse("{% assign oh-my = 'godz' %}{{ oh-my }}").render(), is("godz"));
    }

    /*
     * def test_assign
     *   assigns = {'var' => 'content' }
     *   assert_template_result('var2:  var2:content', 'var2:{{var2}} {%assign var2 = var%} var2:{{var2}}', assigns)
     *
     * end
     */
    @Test
    public void assignTest() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("var2:{{var2}} {%assign var2 = var%} var2:{{var2}}")
                        .render(" { \"var\" : \"content\" } "),
                is("var2:  var2:content"));
    }

    /* def test_hyphenated_assign
     *   assigns = {'a-b' => '1' }
     *   assert_template_result('a-b:1 a-b:2', 'a-b:{{a-b}} {%assign a-b = 2 %}a-b:{{a-b}}', assigns)
     *
     * end
     */
    @Test
    public void hyphenated_assignTest() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("a-b:{{a-b}} {%assign a-b = 2 %}a-b:{{a-b}}")
                        .render(" { \"a-b\" : \"1\" } "),
                is("a-b:1 a-b:2"));
    }

    /*
     * def test_assign_with_colon_and_spaces
     *   assigns = {'var' => {'a:b c' => {'paged' => '1' }}}
     *   assert_template_result('var2: 1', '{%assign var2 = var["a:b c"].paged %}var2: {{var2}}', assigns)
     * end
     */
    @Test
    public void assign_with_colon_and_spacesTest() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("{%assign var2 = var[\"a:b c\"].paged %}var2: {{var2}}")
                        .render("{\"var\" : {\"a:b c\" : {\"paged\" : \"1\" }}}"),
                is("var2: 1"));
    }

    /*
     * def test_assign
     *   assert_equal 'variable', Liquid::TemplateFactory.newBuilder().parse( '{% assign a = "variable"%}{{a}}'  ).render
     * end
     */
    @Test
    public void assign2Test() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("{% assign a = \"variable\"%}{{a}}")
                        .render(),
                is("variable"));
    }

    /*
     * def test_assign_an_empty_string
     *   assert_equal '', Liquid::TemplateFactory.newBuilder().parse( '{% assign a = ""%}{{a}}'  ).render
     * end
     */
    @Test
    public void assign_an_empty_stringTest() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("{% assign a = \"\"%}{{a}}")
                        .render(),
                is(""));
    }

    /*
     * def test_assign_is_global
     *   assert_equal 'variable',
     *                Liquid::TemplateFactory.newBuilder().parse( '{%for i in (1..2) %}{% assign a = "variable"%}{% endfor %}{{a}}'  ).render
     * end
     */
    @Test
    public void assign_is_globalTest() throws Exception {

        assertThat(
                TemplateFactory.newBuilder().parse("{%for i in (1..2) %}{% assign a = \"variable\"%}{% endfor %}{{a}}")
                        .render(),
                is("variable"));
    }
}
