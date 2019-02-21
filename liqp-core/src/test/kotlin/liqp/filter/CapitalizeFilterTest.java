package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runners.Parameterized;

public class CapitalizeFilterTest extends LiquifyNoInputTest {

  @Parameterized.Parameters
  public static Object[] testParams() {
    return new String[][]{
          {"{{'a' | capitalize}}", "A"},
          {"{{'' | capitalize}}", ""},
          {"{{1 | capitalize}}", "1"},
    };
  }

  public CapitalizeFilterTest(@NotNull String templateString,
                              @NotNull String expectedResult) {
    super(templateString, expectedResult, null);
  }

  /*
   *
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = new CapitalizeFilter();

    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "testing"), is("Testing"));
    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), null), nullValue());
  }
}
