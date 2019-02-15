package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class StripTest {

    /*
        def test_strip
          assert_template_result 'ab c', "{{ source | strip }}", 'source' => " ab c  "
          assert_template_result 'ab c', "{{ source | strip }}", 'source' => " \tab c  \n \t"
        end
    */
    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ source | strip }}", "", "{ \"source\": null }"},
                {"{{ source | strip }}", "ab c", "{ \"source\": \" ab c  \" }"},
                {"{{ source | strip }}", "ab c", "{ \"source\": \" \\tab c  \\n \\t\" }"},
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.renderJson(test[2]);

            assertThat(rendered, is(test[1]));
        }
    }
}
