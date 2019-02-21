package liqp.filter;

import liqp.parameterized.LiquifyWithInputTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DefaultFilterTest extends LiquifyWithInputTest {
  public DefaultFilterTest(@NotNull String templateString,
                           @Nullable String expectedResult,
                           @Nullable Object inputData) {
    super(templateString, expectedResult, inputData);
  }
  /*
        def test_default
          assert_equal "foo", @filter.default("foo", "bar")
          assert_equal "bar", @filter.default(nil, "bar")
          assert_equal "bar", @filter.default("", "bar")
          assert_equal "bar", @filter.default(false, "bar")
          assert_equal "bar", @filter.default([], "bar")
          assert_equal "bar", @filter.default({}, "bar")
        end
    */

  @Parameterized.Parameters(name="{0}={1}")
  public static  Object[] testParams() {
    return new String[][]{
          {"{{ a | default: b }}", "foo", "{ \"a\": \"foo\", \"b\": \"bar\" }"},
          {"{{ a | default: b }}", "bar", "{ \"a\": null, \"b\": \"bar\" }"},
          {"{{ a | default: b }}", "bar", "{ \"a\": \"\", \"b\": \"bar\" }"},
          {"{{ a | default: b }}", "bar", "{ \"a\": false, \"b\": \"bar\" }"},
          {"{{ a | default: b }}", "bar", "{ \"a\": [], \"b\": \"bar\" }"},
          {"{{ a | default: b }}", "bar", "{ \"a\": {}, \"b\": \"bar\" }"},
          {"{{ a | default }}", "", "{ \"a\": null, \"b\": \"bar\" }"}
    };
  }
}
