package liqp.filter;

import liqp.LiquidParser;
import liqp.Template;
import liqp.parameterized.LiquifyWithInputTest;
import org.junit.Test;

public class ConcatTest extends LiquifyWithInputTest {

    /*
        def test_concat
          assert_equal [1, 2, 3, 4], @filter.concat([1, 2], [3, 4])
          assert_equal [1, 2, 'a'],  @filter.concat([1, 2], ['a'])
          assert_equal [1, 2, 10],   @filter.concat([1, 2], [10])
        end
    */

  public static Object[] testParams() {
    return new String[][]{
          {"{{ a | concat: b }}", "1234", "{ \"a\": [1, 2], \"b\": [3, 4], \"c\": \"FOO\" }"},
          {"{{ a | concat: b }}", "12X", "{ \"a\": [1, 2], \"b\": [\"X\"], \"c\": \"FOO\" }"},
          {"{{ a | concat: b }}", "1210", "{ \"a\": [1, 2], \"b\": [10], \"c\": \"FOO\" }"},
          {"{{ c | concat: b }}", "FOO34", "{ \"a\": [1, 2], \"b\": [3, 4], \"c\": \"FOO\" }"},
          {"{{ c | concat }}", "FOO", "{ \"a\": [1, 2], \"b\": [3, 4], \"c\": \"FOO\" }"},
          {"{{ a | concat: c }}", "12FOO", "{ \"a\": [1, 2], \"b\": [3, 4], \"c\": \"FOO\" }"},
    };
  }

}
