package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UpcaseTest extends LiquifyNoInputTest {
  @Override
  public Object[] testParams() {

    String[][] tests = {
          {"{{ '' | upcase }}", ""},
          {"{{ nil | upcase }}", ""},
          {"{{ 'Abc' | upcase }}", "ABC"},
          {"{{ 'abc' | upcase }}", "ABC"},
    };

    return tests;
  }

  /*
   * def test_upcase
   *   assert_equal 'TESTING', @filter.upcase("Testing")
   *   assert_equal '', @filter.upcase(nil)
   * end
   */
  @Test
  public void applyOriginalTest() {

    final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("upcase");

    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "Testing"), is("TESTING"));
    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), null), nullValue());
  }
}
