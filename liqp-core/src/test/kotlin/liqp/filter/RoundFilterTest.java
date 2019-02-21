package liqp.filter;

import liqp.parameterized.LiquifyWithInputTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runners.Parameterized;

public class RoundFilterTest extends LiquifyWithInputTest {

  public RoundFilterTest(@NotNull String templateString,
                         @Nullable String expectedResult,
                         @Nullable Object inputData) {
    super(templateString, expectedResult, inputData);
  }

  /*
        def test_round
          assert_template_result "5", "{{ input | round }}", 'input' => 4.6
          assert_template_result "4", "{{ '4.3' | round }}"
          assert_template_result "4.56", "{{ input | round: 2 }}", 'input' => 4.5612
          assert_template_result "5", "{{ price | round }}", 'price' => NumberLikeThing.new(4.6)
          assert_template_result "4", "{{ price | round }}", 'price' => NumberLikeThing.new(4.3)
        end
    */
  @Parameterized.Parameters(name = "{0}={1}")
  public static Object[] testParams() {
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
