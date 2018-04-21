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

public class Strip_HTMLTest {

    @Test
    public void applyTest() throws RecognitionException {

        String json = "{ \"html\" : \"1<h>2</h>3\" }";

        String[][] tests = {
                {"{{ nil | strip_html }}", ""},
                {"{{ 456 | strip_html }}", "456"},
                {"{{ '45<6' | strip_html }}", "45<6"},
                {"{{ '<a>' | strip_html }}", ""},
                {"{{ html | strip_html }}", "123"},
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render(json));

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_strip_html
     *   assert_equal 'test', @filters.strip_html("<div>test</div>")
     *   assert_equal 'test', @filters.strip_html("<div id='test'>test</div>")
     *   assert_equal '', @filters.strip_html("<script type='text/javascript'>document.write('some stuff');</script>")
     *   assert_equal '', @filters.strip_html(nil)
     * end
     */
    @Test
    public void applyOriginalTest() {

        Filter filter = Filter.getFilter("strip_html");
      final RenderContext context = new RenderContext(emptyMap(),
            mock(TemplateFactory.class),
            mock(TemplateEngine.class));
        assertThat(filter.apply(context, "<div>test</div>"), is((Object)"test"));
        assertThat(filter.apply(context, "<div id='test'>test</div>"), is((Object)"test"));
        assertThat(filter.apply(context, "<script type='text/javascript'>document.write('some stuff');</script>"), is((Object)""));
        assertThat(filter.apply(context, null), is((Object)""));
    }
}
