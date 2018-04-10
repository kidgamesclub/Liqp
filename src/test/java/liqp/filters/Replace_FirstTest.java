package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class Replace_FirstTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ '' | replace_first:'a', 'A' }}", ""},
                {"{{ nil | replace_first:'a', 'A' }}", ""},
                {"{{ 'aabbabab' | replace_first:'ab', 'A' }}", "aAbabab"},
                {"{{ 'ababab' | replace_first:'a', 'A' }}", "Ababab"},
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    @Test(expected = RuntimeException.class)
    public void applyTestInvalidPattern1() throws RecognitionException {
        TemplateFactory.newBuilder().parse("{{ 'ababab' | replace_first:nil, 'A' }}").render();
    }

    @Test(expected = RuntimeException.class)
    public void applyTestInvalidPattern2() throws RecognitionException {
        TemplateFactory.newBuilder().parse("{{ 'ababab' | replace_first:'a', nil }}").render();
    }

    /*
     * def test_replace
     *   assert_equal 'b b b b', @filters.replace("a a a a", 'a', 'b')
     *   assert_equal 'b a a a', @filters.replace_first("a a a a", 'a', 'b')
     *   assert_template_result 'b a a a', "{{ 'a a a a' | replace_first: 'a', 'b' }}"
     * end
     */
    @Test
    public void applyOriginalTest() {

        Filter filter = Filter.getFilter("replace_first");

        assertThat(filter.apply("a a a a", "a", "b"), is((Object)"b a a a"));
        assertThat(TemplateFactory.newBuilder().parse("{{ 'a a a a' | replace_first: 'a', 'b' }}").render(), is("b a a a"));
    }
}
