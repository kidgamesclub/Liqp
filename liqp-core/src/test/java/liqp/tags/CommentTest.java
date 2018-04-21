package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class CommentTest {

    @Test
    public void renderTest() throws RecognitionException {

        String[][] tests = {
                {"{% comment %}ABC{% endcomment %}", ""},
                {"A{% comment %}B{% endcomment %}C", "AC"}
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_has_a_block_which_does_nothing
     *   assert_template_result(%|the comment block should be removed  .. right?|,
     *                          %|the comment block should be removed {%comment%} be gone.. {%endcomment%} .. right?|)
     *
     *   assert_template_result('','{%comment%}{%endcomment%}')
     *   assert_template_result('','{%comment%}{% endcomment %}')
     *   assert_template_result('','{% comment %}{%endcomment%}')
     *   assert_template_result('','{% comment %}{% endcomment %}')
     *   assert_template_result('','{%comment%}comment{%endcomment%}')
     *   assert_template_result('','{% comment %}comment{% endcomment %}')
     *
     *   assert_template_result('foobar','foo{%comment%}comment{%endcomment%}bar')
     *   assert_template_result('foobar','foo{% comment %}comment{% endcomment %}bar')
     *   assert_template_result('foobar','foo{%comment%} comment {%endcomment%}bar')
     *   assert_template_result('foobar','foo{% comment %} comment {% endcomment %}bar')
     *
     *   assert_template_result('foo  bar','foo {%comment%} {%endcomment%} bar')
     *   assert_template_result('foo  bar','foo {%comment%}comment{%endcomment%} bar')
     *   assert_template_result('foo  bar','foo {%comment%} comment {%endcomment%} bar')
     *
     *   assert_template_result('foobar','foo{%comment%}
     *                                    {%endcomment%}bar')
     * end
     */
    @Test
    public void has_a_block_which_does_nothingTest() throws RecognitionException {

        assertThat(LiquidParser.newInstance().parse("the comment block should be removed {%comment%} be gone.. {%endcomment%} .. right?").render(),
                is("the comment block should be removed  .. right?"));

        assertThat(LiquidParser.newInstance().parse("{%comment%}{%endcomment%}").render(), is(""));
        assertThat(LiquidParser.newInstance().parse("{%comment%}{% endcomment %}").render(), is(""));
        assertThat(LiquidParser.newInstance().parse("{% comment %}{%endcomment%}").render(), is(""));
        assertThat(LiquidParser.newInstance().parse("{% comment %}{% endcomment %}").render(), is(""));
        assertThat(LiquidParser.newInstance().parse("{%comment%}comment{%endcomment%}").render(), is(""));
        assertThat(LiquidParser.newInstance().parse("{% comment %}comment{% endcomment %}").render(), is(""));

        assertThat(LiquidParser.newInstance().parse("foo{%comment%}comment{%endcomment%}bar").render(), is("foobar"));
        assertThat(LiquidParser.newInstance().parse("foo{% comment %}comment{% endcomment %}bar").render(), is("foobar"));
        assertThat(LiquidParser.newInstance().parse("foo{%comment%} comment {%endcomment%}bar").render(), is("foobar"));
        assertThat(LiquidParser.newInstance().parse("foo{% comment %} comment {% endcomment %}bar").render(), is("foobar"));

        assertThat(LiquidParser.newInstance().parse("foo {%comment%} {%endcomment%} bar").render(), is("foo  bar"));
        assertThat(LiquidParser.newInstance().parse("foo {%comment%}comment{%endcomment%} bar").render(), is("foo  bar"));
        assertThat(LiquidParser.newInstance().parse("foo {%comment%} comment {%endcomment%} bar").render(), is("foo  bar"));

        assertThat(LiquidParser.newInstance().parse("foo{%comment%}\n         {%endcomment%}bar").render(), is("foobar"));
    }
}
