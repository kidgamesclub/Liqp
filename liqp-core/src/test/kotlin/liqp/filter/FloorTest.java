package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidParser;
import liqp.LiquidTemplate;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class FloorTest {

  /*
      def test_floor
        assert_template_result "4", "{{ input | floor }}", 'input' => 4.6
        assert_template_result "4", "{{ '4.3' | floor }}"
        assert_template_result "5", "{{ price | floor }}", 'price' => NumberLikeThing.new(5.4)
      end
  */
  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ input | floor }}", "4", "{ \"input\": 4.6 }"},
          {"{{ '4.3' | floor }}", "4", "{}"},
          {"{{ price | floor }}", "5", "{ \"price\": 5.4 }"}
    };

    for (String[] test : tests) {

      LTemplate template = createTestParser().parse(test[0]);
      String rendered = template.renderJson(test[2]);

      assertThat(rendered, is(test[1]));
    }
  }
}
