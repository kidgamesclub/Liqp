package liqp.filter;

import static liqp.AssertsKt.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
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

      LTemplate template = createTestParser().parse(test[0]);
      String rendered = template.renderJson(test[2]);

      assertThat(rendered, is(test[1]));
    }
  }
}
