package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class UniqFilterTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ x | uniq }}", "", "{ \"x\": [] }"},
                {"{{ x | uniq }}", "true", "{ \"x\": true }"},
                {"{{ x | uniq }}", "mu", "{ \"x\": \"mu\" }"},
                {"{{ x | uniq }}", "", "{ \"x\": null }"},
                {"{{ x | uniq }}", "1342", "{ \"x\": [1, 1, 3, 4, 3, 2, 1, 2, 3, 2, 1, 1, 2] }"},
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.renderJson(test[2]);

            assertThat(rendered, is(test[1]));
        }
    }
}
