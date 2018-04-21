package liqp.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class AndNodeTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{% if 42 and 1234 %}TRUE{% else %}FALSE{% endif %}", "TRUE"},
                {"{% if 'x' and true %}TRUE{% else %}FALSE{% endif %}", "TRUE"},
                {"{% if false and true %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
                {"{% if false and '' %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
                {"{% if nil and true %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
                {"{% if nil and false %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }
}
