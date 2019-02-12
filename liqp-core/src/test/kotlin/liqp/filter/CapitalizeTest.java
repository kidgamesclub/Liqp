package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
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

    LFilter filter = new Capitalize();

    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "testing"), is("Testing"));
    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), null), nullValue());
  }
}
