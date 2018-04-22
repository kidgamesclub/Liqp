package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Mocks;
import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class ReplaceFirstTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ '' | replace_first:'a', 'A' }}", ""},
                {"{{ nil | replace_first:'a', 'A' }}", ""},
                {"{{ 'aabbabab' | replace_first:'ab', 'A' }}", "aAbabab"},
                {"{{ 'ababab' | replace_first:'a', 'A' }}", "Ababab"},
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    @Test(expected = RuntimeException.class)
    public void applyTestInvalidPattern1() throws RecognitionException {
        LiquidParser.newInstance().parse("{{ 'ababab' | replace_first:nil, 'A' }}").render();
    }

    @Test(expected = RuntimeException.class)
    public void applyTestInvalidPattern2() throws RecognitionException {
        LiquidParser.newInstance().parse("{{ 'ababab' | replace_first:'a', nil }}").render();
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

        assertThat(filter.apply(Mocks.mockRenderContext(), "a a a a", "a", "b"), is((Object)"b a a a"));
        assertThat(LiquidParser.newInstance().parse("{{ 'a a a a' | replace_first: 'a', 'b' }}").render(), is("b a a a"));
    }
}
