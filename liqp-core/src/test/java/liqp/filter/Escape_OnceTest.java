package liqp.filter;

import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;

public class Escape_OnceTest extends LiquifyNoInputTest {
  public static Object[] testParams() {
    return new String[][]{
          {"{{ nil | escape_once }}", ""},
          {"{{ 42 | escape_once }}", "42"},
          {"{{ n | escape_once }}", "12345"},
          {"{{ '<foo>&\"' | escape_once }}", "&lt;foo&gt;&amp;&quot;"},
          {"{{ false | escape_once }}", "false"},
          {"{{ '&&amp;' | escape_once }}", "&amp;&amp;"},
    };
  }

  public Escape_OnceTest() {
    super("{ \"n\" : [1,2,3,4,5] }");
  }

  /*
   * def test_escape_once
   *   assert_equal '&lt;strong&gt;', @filter.escape_once(@filter.escape('<strong>'))
   * end
   */
  @Test
  public void applyOriginalTest() {

    final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("escape_once");

    assertThat(filter.doPostFilter(mockRenderContext(), LiquidDefaults.getDefaultFilters().getFilter("escape").apply
          (mockRenderContext(), "<strong>")), is((Object) "&lt;" +
          "strong&gt;"));

    // the same test:
    assertThat(filter.doPostFilter(mockRenderContext(), "&lt;strong&gt;"), is((Object) "&lt;strong&gt;"));
  }
}
