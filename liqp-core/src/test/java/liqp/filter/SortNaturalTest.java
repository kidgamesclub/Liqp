package liqp.filter;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyWithInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class SortNaturalTest extends LiquifyWithInputTest {

  /*
      def test_sort_natural_empty_array
        assert_equal [], @filter.sort_natural([], "a")
      end
  */
  public Object[] testParams() {

    String[][] tests = {
          {"{{ nil | sort_natural }}", "", "{ \"x\": [] }"},
          {"{{ false | sort_natural }}", "false", "{ \"x\": [] }"},
          {"{{ x | sort_natural }}", "42", "{ \"x\": 42 }"},
          {"{{ x | sort_natural }}", "", "{ \"x\": [] }"},
          {"{{ x | sort_natural }}", "99 A00000.0001 a01.1 a04 a1 a10 ", "{ \"x\": [\"a1 \", \"A00000.0001 \", " +
                "\"a01.1 \", \"a10 \", \"a04 \", \"99 \"] }"},
          {"{{ x | sort_natural }}", "01 02 10 100 A b Cccc cccccccc d Ddd ", "{ \"x\": [\"b \", \"A \", \"Cccc \", " +
                "\"cccccccc \", \"Ddd \", \"d \", \"01 \", \"02 \", \"10 \", \"100 \"] }"}
    };

    return tests;
  }
}
