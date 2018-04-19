package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class DefaultTest {

    /*
        def test_default
          assert_equal "foo", @filters.default("foo", "bar")
          assert_equal "bar", @filters.default(nil, "bar")
          assert_equal "bar", @filters.default("", "bar")
          assert_equal "bar", @filters.default(false, "bar")
          assert_equal "bar", @filters.default([], "bar")
          assert_equal "bar", @filters.default({}, "bar")
        end
    */
    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ a | default: b }}", "foo", "{ \"a\": \"foo\", \"b\": \"bar\" }"},
                {"{{ a | default: b }}", "bar", "{ \"a\": null, \"b\": \"bar\" }"},
                {"{{ a | default: b }}", "bar", "{ \"a\": \"\", \"b\": \"bar\" }"},
                {"{{ a | default: b }}", "bar", "{ \"a\": false, \"b\": \"bar\" }"},
                {"{{ a | default: b }}", "bar", "{ \"a\": [], \"b\": \"bar\" }"},
                {"{{ a | default: b }}", "bar", "{ \"a\": {}, \"b\": \"bar\" }"},
                {"{{ a | default }}", "", "{ \"a\": null, \"b\": \"bar\" }"}
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render(test[2]));

            assertThat(rendered, is(test[1]));
        }
    }
}
