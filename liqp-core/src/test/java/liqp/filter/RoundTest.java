package liqp.filter;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyWithInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RoundTest extends LiquifyWithInputTest {

  /*
      def test_round
        assert_template_result "5", "{{ input | round }}", 'input' => 4.6
        assert_template_result "4", "{{ '4.3' | round }}"
        assert_template_result "4.56", "{{ input | round: 2 }}", 'input' => 4.5612
        assert_template_result "5", "{{ price | round }}", 'price' => NumberLikeThing.new(4.6)
        assert_template_result "4", "{{ price | round }}", 'price' => NumberLikeThing.new(4.3)
      end
  */
  public Object[] testParams() {
    return new String[][]{

          {"{{ nil | round }}", "0", "{}"},
          {"{{ 'MU' | round }}", "0", "{}"},
          {"{{ input | round }}", "5", "{ \"input\": 4.6 }"},
          {"{{ '4.3' | round }}", "4", "{}"},
          {"{{ input | round: 2 }}", "4.56", "{ \"input\": 4.5612 }"},
          {"{{ input | round: 2.999 }}", "4.56", "{ \"input\": 4.5612 }"},
          {"{{ price | round }}", "5", "{ \"price\": 4.6 }"},
          {"{{ price | round }}", "4", "{ \"price\": 4.3 }"}
    };
  }
}
