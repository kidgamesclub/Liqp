package liqp.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidTemplate;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class NEqNodeTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{% if 1.0 != 1 %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
                {"{% if nil != nil %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
                {"{% if false != false %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
                {"{% if \"\" != '' %}TRUE{% else %}FALSE{% endif %}", "FALSE"},
        };

        for (String[] test : tests) {

            LiquidTemplate template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }
}
