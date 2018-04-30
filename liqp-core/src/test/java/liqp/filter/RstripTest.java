package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class RstripTest {

    /*
        def test_rstrip
          assert_template_result " ab c", "{{ source | rstrip }}", 'source' => " ab c  "
          assert_template_result " \tab c", "{{ source | rstrip }}", 'source' => " \tab c  \n \t"
        end
    */
    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ source | rstrip }}", "", "{ \"source\": null }"},
                {"{{ source | rstrip }}", " ab c", "{ \"source\": \" ab c  \" }"},
                {"{{ source | rstrip }}", " \tab c", "{ \"source\": \" \\tab c  \\n \\t\" }"},
        };

        for (String[] test : tests) {

            Template template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render(test[2]));

            assertThat(rendered, is(test[1]));
        }
    }
}
