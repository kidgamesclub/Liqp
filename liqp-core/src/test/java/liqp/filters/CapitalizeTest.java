package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class CapitalizeTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{'a' | capitalize}}", "A"},
                {"{{'' | capitalize}}", ""},
                {"{{1 | capitalize}}", "1"},
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     *
     */
    @Test
    public void applyOriginalTest() {

        Filter filter = Filter.getFilter("capitalize");

        assertThat(filter.apply(Mocks.mockRenderContext(), "testing"), is((Object)"Testing"));
        assertThat(filter.apply(Mocks.mockRenderContext(), null), is((Object)""));
    }
}
