package liqp.filter;

import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class EscapeOnceTest extends LiquifyNoInputTest {
  @Parameterized.Parameters
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

  public EscapeOnceTest(@NotNull String templateString,
                        @NotNull String expectedResult) {
    super(templateString, expectedResult, "{ \"n\" : [1,2,3,4,5] }");
  }

  /*
   * def test_escape_once
   *   assert_equal '&lt;strong&gt;', @filter.escape_once(@filter.escape('<strong>'))
   * end
   */
  @Test
  public void applyOriginalTest() {

    final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("escape_once");

    assertThat(filter.onFilterAction(mockRenderContext(),
          LiquidDefaults.getDefaultFilters().getFilter("escape").onFilterAction
          (mockRenderContext(), "<strong>")), is((Object) "&lt;" +
          "strong&gt;"));

    // the same test:
    assertThat(filter.onFilterAction(mockRenderContext(), "&lt;strong&gt;"), is((Object) "&lt;strong&gt;"));
  }
}
