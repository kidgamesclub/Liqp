package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
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

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_upcase
     *   assert_equal 'TESTING', @filters.upcase("Testing")
     *   assert_equal '', @filters.upcase(nil)
     * end
     */
    @Test
    public void applyOriginalTest() {

        final Filter filter = Filter.getFilter("upcase");

        assertThat(filter.apply("Testing"), is((Object)"TESTING"));
        assertThat(filter.apply(null), is((Object)""));
    }
}
