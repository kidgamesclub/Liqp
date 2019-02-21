package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.runners.Parameterized.Parameters;

import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class DowncaseFilterTest extends LiquifyNoInputTest {

  @Parameters
  public static Object[] testParams() {
    return new String[][]{

          {"{{ '' | downcase }}", ""},
          {"{{ nil | downcase }}", ""},
          {"{{ 'Abc' | downcase }}", "abc"},
          {"{{ 'abc' | downcase }}", "abc"},
    };
  }

  public DowncaseFilterTest(@NotNull String templateString, @NotNull String expectedResult) {
    super(templateString, expectedResult);
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
