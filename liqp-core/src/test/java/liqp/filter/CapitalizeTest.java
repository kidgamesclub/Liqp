package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;

public class CapitalizeTest extends LiquifyNoInputTest {

  public Object[] testParams() {
    return new String[][]{
          {"{{'a' | capitalize}}", "A"},
          {"{{'' | capitalize}}", ""},
          {"{{1 | capitalize}}", "1"},
    };
  }

  /*
   *
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("capitalize");

    assertThat(filter.doPostFilter(Mocks.mockRenderContext(), "testing"), is((Object) "Testing"));
    assertThat(filter.doPostFilter(Mocks.mockRenderContext(), null), nullValue());
  }
}
