package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class RemoveTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ '' | remove:'a' }}", ""},
                {"{{ nil | remove:'a' }}", ""},
                {"{{ 'aabb' | remove:'ab' }}", "ab"},
                {"{{ 'ababab' | remove:'a' }}", "bbb"},
        };

        for (String[] test : tests) {

            LiquidTemplate template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    @Test(expected = RuntimeException.class)
    public void applyTestInvalidPattern() throws RecognitionException {
        LiquidParser.newInstance().parse("{{ 'ababab' | remove:nil }}").render();
    }

    /*
     * def test_remove
     *   assert_equal '   ', @filter.remove("a a a a", 'a')
     *   assert_equal 'a a a', @filter.remove_first("a a a a", 'a ')
     *   assert_template_result 'a a a', "{{ 'a a a a' | remove_first: 'a ' }}"
     * end
     */
    @Test
    public void applyOriginalTest() {

        LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("remove");

        assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "a a a a", "a"), is((Object)"   "));
    }
}
