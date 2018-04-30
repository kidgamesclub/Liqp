package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class UpcaseTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ '' | upcase }}", ""},
                {"{{ nil | upcase }}", ""},
                {"{{ 'Abc' | upcase }}", "ABC"},
                {"{{ 'abc' | upcase }}", "ABC"},
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_upcase
     *   assert_equal 'TESTING', @filter.upcase("Testing")
     *   assert_equal '', @filter.upcase(nil)
     * end
     */
    @Test
    public void applyOriginalTest() {

        final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("upcase");

        assertThat(filter.doPostFilter(Mocks.mockRenderContext(), "Testing"), is((Object)"TESTING"));
        assertThat(filter.doPostFilter(Mocks.mockRenderContext(), null), is((Object)""));
    }
}
