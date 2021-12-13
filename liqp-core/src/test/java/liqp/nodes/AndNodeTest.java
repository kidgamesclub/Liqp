package liqp.nodes;

import static liqp.AssertsKt.*;
import static org.hamcrest.CoreMatchers.is;

import liqp.AssertsKt;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
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

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.render();

            MatcherAssert.assertThat(rendered, is(test[1]));
        }
    }
}
