package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class JoinFilterTest {

    @Test
    public void applyTest() throws RecognitionException {

        String json = "{ \"array\" : [\"x\", \"y\", \"z\"] }";

        String[][] tests = {
                {"{{ array | join }}", "x y z"},
                {"{{ array | join:'' }}", "xyz"},
                {"{{ array | join:'@@@' }}", "x@@@y@@@z"},
                {"{{ x | join:'@@@' }}", ""},
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = String.valueOf(template.renderJson(json));

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_join
     *   assert_equal '1 2 3 4', @filter.join([1,2,3,4])
     *   assert_equal '1 - 2 - 3 - 4', @filter.join([1,2,3,4], ' - ')
     * end
     */
    @Test
    public void applyOriginalTest() {

        LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("join");

        assertThat(filter.onFilterAction(Mocks.mockRenderContext(), new Integer[]{1,2,3,4}), is((Object)"1 2 3 4"));
        assertThat(filter.onFilterAction(Mocks.mockRenderContext(), new Integer[]{1,2,3,4}, " - "), is((Object)"1 - 2 - 3 - 4"));
    }
}