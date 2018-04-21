package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
     *   assert_equal 1, @filters.first([1,2,3])
     *   assert_equal 3, @filters.last([1,2,3])
     *   assert_equal nil, @filters.first([])
     *   assert_equal nil, @filters.last([])
     * end
     */
    @Test
    public void applyOriginalTest() {

        Filter filter = Filter.getFilter("last");

        assertThat(filter.apply(Mocks.mockRenderContext(), new Integer[]{1, 2, 3}), is((Object)"3"));
        assertThat(filter.apply(Mocks.mockRenderContext(), new Integer[]{}), is((Object)null));
    }
}
