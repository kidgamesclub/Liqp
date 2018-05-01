package liqp.filter;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyWithInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AtMostTest extends LiquifyWithInputTest {

  /*
      def test_at_most
        assert_template_result "4", "{{ 5 | at_most:4 }}"
        assert_template_result "5", "{{ 5 | at_most:5 }}"
        assert_template_result "5", "{{ 5 | at_most:6 }}"

        assert_template_result "4.5", "{{ 4.5 | at_most:5 }}"
        assert_template_result "5", "{{ width | at_most:5 }}", 'width' => NumberLikeThing.new(6)
        assert_template_result "4", "{{ width | at_most:5 }}", 'width' => NumberLikeThing.new(4)
        assert_template_result "4", "{{ 5 | at_most: width }}", 'width' => NumberLikeThing.new(4)
      end
  */
  public Object[] testParams() {
    return new String[][]{

          {"{{ 5 | at_most:4 }}",  "4", "{}"},
          {"{{ 5 | at_most:5 }}",  "5", "{}"},
          {"{{ 5 | at_most:6 }}", "5", "{}"},

          {"{{ 4.5 | at_most:5 }}", "4.5", "{}"},
          {"{{ width | at_most:5 }}", "5", "{ \"width\": 6 }"},
          {"{{ width | at_most:5 }}", "4", "{ \"width\": 4 }"},
          {"{{ 5 | at_most: width }}", "4", "{ \"width\": 4 }"}
    };
  }
}
