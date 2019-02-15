package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class ReverseTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ values | reverse }}", "", "{ \"values\": null }" },
                {"{{ values | reverse }}", "MU", "{ \"values\": \"MU\" }" },
                {"{{ values | reverse }}", "1", "{ \"values\": [1] }" },
                {"{{ values | reverse }}", "21", "{ \"values\": [1,2] }" },
                {"{{ values | reverse }}", "321", "{ \"values\": [1,2,3] }" },
                {"{{ values | reverse }}", "4321", "{ \"values\": [1,2,3,4] }" },
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.renderJson(test[2]);

            assertThat(rendered, is(test[1]));
        }
    }
}
