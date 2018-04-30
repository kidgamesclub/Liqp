package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.Parameters;
import liqp.LiquidParser;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;

public class AppendTest extends LiquifyNoInputTest {

  public static Object[] testParams() {
    return new String[][]{
          {"{{ 'a' | append: 'b' }}", "ab"},
          {"{{ '' | append: '' }}", ""},
          {"{{ 1 | append: 23 }}", "123"},
          {"{{ nil | append: 'a' }}", "a"},
          {"{{ nil | append: nil }}", ""},
    };
  }

  @Parameters(method = "testParams")
  @Test
  @Override
  public void run(String templateString, Object expectedResult) {
    super.run(templateString, expectedResult);
  }

  /*
   * def test_append
   *   assigns = {'a' => 'bc', 'b' => 'd' }
   *   assert_template_result('bcd',"{{ a | append: 'd'}}",assigns)
   *   assert_template_result('bcd',"{{ a | append: b}}",assigns)
   * end
   */
  @Test
  public void applyOriginalTest() {

    final String assigns = "{\"a\":\"bc\", \"b\":\"d\" }";

    assertThat(LiquidParser.newInstance().parse("{{ a | append: 'd'}}").render(assigns), is("bcd"));
    assertThat(LiquidParser.newInstance().parse("{{ a | append: b}}").render(assigns), is("bcd"));
  }
}
