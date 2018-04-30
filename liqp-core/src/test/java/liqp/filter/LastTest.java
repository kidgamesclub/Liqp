package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class LastTest {

    @Test
    public void applyTest() throws RecognitionException {

        Template template = LiquidParser.newInstance().parse("{{values | last}}");

        String rendered = String.valueOf(template.render("{\"values\" : [\"Mu\", \"foo\", \"bar\"]}"));

        assertThat(rendered, is("bar"));
    }

    /*
     * def test_first_last
     *   assert_equal 1, @filter.first([1,2,3])
     *   assert_equal 3, @filter.last([1,2,3])
     *   assert_equal nil, @filter.first([])
     *   assert_equal nil, @filter.last([])
     * end
     */
    @Test
    public void applyOriginalTest() {

        LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("last");

        assertThat(filter.doPostFilter(Mocks.mockRenderContext(), new Integer[]{1, 2, 3}), is(3));
        assertThat(filter.doPostFilter(Mocks.mockRenderContext(), new Integer[]{}), is((Object)null));
    }
}
