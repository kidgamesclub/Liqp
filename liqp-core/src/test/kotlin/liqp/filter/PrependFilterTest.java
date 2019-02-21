package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class PrependFilterTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ 'a' | prepend: 'b' }}", "ba"},
                {"{{ '' | prepend: '' }}", ""},
                {"{{ 1 | prepend: 23 }}", "231"},
                {"{{ nil | prepend: 'a' }}", "a"},
                {"{{ nil | prepend: nil }}", ""},
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.render();

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_prepend
     *   assigns = {'a' => 'bc', 'b' => 'a' }
     *   assert_template_result('abc',"{{ a | prepend: 'a'}}",assigns)
     *   assert_template_result('abc',"{{ a | prepend: b}}",assigns)
     * end
     */
    @Test
    public void applyOriginalTest() {

        final String json = "{ \"a\":\"bc\", \"b\":\"a\" }";

        assertThat(createTestParser().parse("{{ a | prepend: 'a'}}").renderJson(json), is("abc"));
        assertThat(createTestParser().parse("{{ a | prepend: b}}").renderJson(json), is("abc"));
    }
}
