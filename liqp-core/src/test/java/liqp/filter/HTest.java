package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class HTest extends LiquifyNoInputTest {
  public Object[] testParams() {
    return new String[][]{

          {"{{ nil | h }}", ""},
          {"{{ 42 | h }}", "42"},
          {"{{ n | h }}", "12345"},
          {"{{ '<foo>&\"' | h }}", "&lt;foo&gt;&amp;&quot;"},
          {"{{ false | h }}", "false"},
    };
  }

  public HTest() {
    super("{ \"n\" : [1,2,3,4,5] }");
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
