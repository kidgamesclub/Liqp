package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.junit.Test;

public class DecrementTest {

    @Test
    public void testDec() {

        String[][] tests = {
                {"{%decrement port %}", "-1"},
                {"{%decrement port %} {%decrement port%}", "-1 -2"},
                {"{%decrement port %} {%decrement starboard%} {%decrement port %} {%decrement port%} {%decrement starboard %}", "-1 -1 -2 -3 -2"},
                {"{% assign x = 42 %}{{x}} {%decrement x %} {%decrement x %} {{x}}", "42 -1 -2 42"},
                {"{% decrement x %} {% decrement x %} {{x}}", "-1 -2 -2"}
        };

        for (String[] test : tests) {

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }
}
