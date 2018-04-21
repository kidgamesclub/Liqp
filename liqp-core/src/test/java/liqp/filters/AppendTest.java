package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class AppendTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ 'a' | append: 'b' }}", "ab"},
                {"{{ '' | append: '' }}", ""},
                {"{{ 1 | append: 23 }}", "123"},
                {"{{ nil | append: 'a' }}", "a"},
                {"{{ nil | append: nil }}", ""},
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_append
     *   assigns = {'a' => 'bc', 'b' => 'd' }
     *   assert_template_result('bcd',"{{ a | append: 'd'}}",assigns)
     *   assert_template_result('bcd',"{{ a | append: b}}",assigns)
     * end
     */
    @Test
    public void applyOriginalTest() {

        final String assigns = "{\"a\":\"bc\", \"b\":\"d\" }";

        assertThat(TemplateFactory.newBuilder().parse("{{ a | append: 'd'}}").render(assigns), is("bcd"));
        assertThat(TemplateFactory.newBuilder().parse("{{ a | append: b}}").render(assigns), is("bcd"));
    }
}
