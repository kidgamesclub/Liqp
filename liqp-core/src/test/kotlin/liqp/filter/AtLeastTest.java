package liqp.filter;

import liqp.parameterized.LiquifyWithInputTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AtLeastTest extends LiquifyWithInputTest {
  public AtLeastTest(@NotNull String templateString,
                     @Nullable String expectedResult,
                     @Nullable Object inputData) {
    super(templateString, expectedResult, inputData);
  }

  @Parameterized.Parameters(name = "{0}={1}")
  public static Object[] testParams() {
    return new String[][]{

          /*
              def test_at_least
                assert_template_result "5", "{{ 5 | at_least:4 }}"
                assert_template_result "5", "{{ 5 | at_least:5 }}"
                assert_template_result "6", "{{ 5 | at_least:6 }}"

                assert_template_result "5", "{{ 4.5 | at_least:5 }}"
                assert_template_result "6", "{{ width | at_least:5 }}", 'width' => NumberLikeThing.new(6)
                assert_template_result "5", "{{ width | at_least:5 }}", 'width' => NumberLikeThing.new(4)
                assert_template_result "6", "{{ 5 | at_least: width }}", 'width' => NumberLikeThing.new(6)
              end
          */

          {"{{ 5 | at_least:4 }}", "5", "{}"},
          {"{{ 5 | at_least:5 }}", "5", "{}"},
          {"{{ 5 | at_least:6 }}", "6", "{}"},

          {"{{ 4.5 | at_least:5 }}", "5", "{}"},
          {"{{ width | at_least:5 }}", "6", "{ \"width\": 6 }"},
          {"{{ width | at_least:5 }}", "5", "{ \"width\": 4 }"},
          {"{{ 5 | at_least: width }}", "6", "{ \"width\": 6 }"}
    };
  }
}
