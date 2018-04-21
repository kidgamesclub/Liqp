package liqp.filters;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import liqp.Template;
import liqp.TemplateEngine;
import liqp.TemplateFactory;
import liqp.nodes.RenderContext;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class FirstTest {

    @Test
    public void applyTest() throws RecognitionException {

        Template template = TemplateFactory.newBuilder().parse("{{values | first}}");

        String rendered = String.valueOf(template.render("{\"values\" : [\"Mu\", \"foo\", \"bar\"]}"));

        assertThat(rendered, is("Mu"));
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

        Filter filter = Filter.getFilter("first");

      final RenderContext context = new RenderContext(emptyMap(),
            mock(TemplateFactory.class),
            mock(TemplateEngine.class));

        assertThat(filter.apply(context, new Integer[]{1, 2, 3}), is((Object)"1"));
        assertThat(filter.apply(context, new Integer[]{}), is((Object)null));
    }
}
