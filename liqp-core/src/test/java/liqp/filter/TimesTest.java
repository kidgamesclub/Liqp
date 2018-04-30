package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class TimesTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ 8 | times: 2 }}", "16"},
                {"{{ 8 | times: 3 }}", "24"},
                {"{{ 8 | times: 3. }}", "24.0"},
                {"{{ 8 | times: '3.0' }}", "24.0"},
                {"{{ 8 | times: 2.0 }}", "16.0"},
                {"{{ foo | times: 4 }}", "0"},
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    @Test(expected=RuntimeException.class)
    public void applyTestInvalid1() {
        LiquidDefaults.getDefaultFilters().getFilter("times").apply(Mocks.mockRenderContext(), 1);
    }

    @Test(expected=RuntimeException.class)
    public void applyTestInvalid2() {
        LiquidDefaults.getDefaultFilters().getFilter("times").apply(Mocks.mockRenderContext(), 1, 2, 3);
    }

    /*
     * def test_times
     *   assert_template_result "12", "{{ 3 | times:4 }}"
     *   assert_template_result "0", "{{ 'foo' | times:4 }}"
     *
     *   # Ruby v1.9.2-rc1, or higher, backwards compatible Float test
     *   assert_match(/(6\.3)|(6\.(0{13})1)/, TemplateFactory.newInstance().parse("{{ '2.1' | times:3 }}").render)
     *
     *   assert_template_result "6", "{{ '2.1' | times:3 | replace: '.','-' | plus:0}}"
     * end
     */
    @Test
    public void applyOriginalTest() {

        LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("times");

        assertThat(filter.doPostFilter(Mocks.mockRenderContext(), 3L, 4L), is((Object)12L));
        // assert_template_result "0", "{{ 'foo' | times:4 }}" // see: applyTest()
        assertTrue(String.valueOf(filter.doPostFilter(Mocks.mockRenderContext(), 2.1, 3L)).matches("6[.,]30{10,}1"));
    }
}
