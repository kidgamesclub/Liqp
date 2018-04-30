package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class CompactTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ values | compact }}", "", "{ \"values\": [] }"},
          {"{{ values | compact }}", "123", "{ \"values\": [1, 2, 3] }"},
          {"{{ values | compact }}", "123", "{ \"values\": [\"1\", \"2\", \"3\"] }"},
          {"{{ values | compact }}", "123", "{ \"values\": [null, \"1\", \"\", \"2\", null, \"3\"] }"}
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render(test[2]));

      assertThat(rendered, is(test[1]));
    }
  }
}
