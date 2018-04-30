package liqp.filter;

import liqp.parameterized.LiquifyWithInputTest;

public class DefaultTest extends LiquifyWithInputTest {
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

  public static Object[] testParams() {
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
