package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DowncaseTest extends LiquifyNoInputTest {

  public Object[] testParams() {
    return new String[][]{

          {"{{ '' | downcase }}", ""},
          {"{{ nil | downcase }}", ""},
          {"{{ 'Abc' | downcase }}", "abc"},
          {"{{ 'abc' | downcase }}", "abc"},
    };
  }

  /*
   * def test_downcase
   *   assert_equal 'testing', @filter.downcase("Testing")
   *   assert_equal '', @filter.downcase(nil)
   * end
   */
  @Test
  public void applyOriginalTest() {

    final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("downcase");
    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "Testing"), is((Object) "testing"));
    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), null), nullValue());
  }
}
