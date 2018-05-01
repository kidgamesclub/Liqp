package liqp.filter;

import liqp.parameterized.LiquifyNoInputTest;

public class AtLeastTest extends LiquifyNoInputTest {
  public String[][] testParams() {
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
