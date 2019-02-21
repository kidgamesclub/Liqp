package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class HFilterTest extends LiquifyNoInputTest {
  public HFilterTest(@NotNull String templateString,
                     @NotNull String expectedResult) {
    super(templateString, expectedResult, "{ \"n\" : [1,2,3,4,5] }");
  }

  @Parameterized.Parameters public static Object[] testParams() {
    return new String[][]{

          {"{{ nil | h }}", ""},
          {"{{ 42 | h }}", "42"},
          {"{{ n | h }}", "12345"},
          {"{{ '<foo>&\"' | h }}", "&lt;foo&gt;&amp;&quot;"},
          {"{{ false | h }}", "false"},
    };
  }

  /*
   * def test_escape
   *   assert_equal '&lt;strong&gt;', @filter.escape('<strong>')
   *   assert_equal '&lt;strong&gt;', @filter.h('<strong>')
   * end
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("h");

    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "<strong>"), is((Object) "&lt;strong&gt;"));
  }
}
