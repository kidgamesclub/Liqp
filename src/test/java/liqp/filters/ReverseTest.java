package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
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

            Template template = TemplateFactory.newBuilder().parse(test[0]);
            String rendered = String.valueOf(template.render(test[2]));

            assertThat(rendered, is(test[1]));
        }
    }
}
