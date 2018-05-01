package liqp.filter;

import static liqp.LiquidDefaults.getDefaultFilters;
import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import junitparams.JUnitParamsRunner;
import liqp.exceptions.LiquidRenderingException;
import liqp.parameterized.LiquifyNoInputTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DividedByTest extends LiquifyNoInputTest {

  public Object[] testParams() {
    String[][] tests = {
          {"{{ 8 | divided_by: 2 }}", "4"},
          {"{{ 8 | divided_by: 3 }}", "2"},
          {"{{ 8 | divided_by: 3. }}", String.valueOf(8 / 3.0)},
          {"{{ 8 | divided_by: 3.0 }}", String.valueOf(8 / 3.0)},
          {"{{ 8 | divided_by: 2.0 }}", "4.0"},
          {"{{ 0 | divided_by: 2.0 }}", "0.0"},
    };
    return tests;
  }

  @Test
  public void applyTestInvalid1() {
    Assertions.assertThat(getDefaultFilters()
          .getFilter("divided_by")
          .onFilterAction(mockRenderContext(), 1))
          .isEqualTo(1);
  }

  @Test
  public void applyTestInvalid2() {
    final LFilter filter = getDefaultFilters().getFilter("divided_by");
    Assertions.assertThat(filter.onFilterAction(mockRenderContext(), 1, 2.0, 3))
          .isEqualTo(1.0 / 6);
  }

  @Test(expected = LiquidRenderingException.class)
  public void applyTestInvalid3() {
    getDefaultFilters().getFilter("divided_by").onFilterAction(mockRenderContext(), 15L, 0L);
  }

  /*
   * def test_divided_by
   *   assert_template_result "4", "{{ 12 | divided_by:3 }}"
   *   assert_template_result "4", "{{ 14 | divided_by:3 }}"
   *
   *   # Ruby v1.9.2-rc1, or higher, backwards compatible Float test
   *   assert_match(/4\.(6{13,14})7/, TemplateFactory.newInstance().parse("{{ 14 | divided_by:'3.0' }}").render)
   *
   *   assert_template_result "5", "{{ 15 | divided_by:3 }}"
   *   assert_template_result "Liquid error: divided by 0", "{{ 5 | divided_by:0 }}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = getDefaultFilters().getFilter("divided_by");

    assertThat(filter.onFilterAction(mockRenderContext(), 12L, 3L), is(4L));
    assertThat(filter.onFilterAction(mockRenderContext(), 14L, 3L), is(4L));
    assertTrue(String.valueOf(filter.onFilterAction(mockRenderContext(), 14L, 3.0)).matches("4[,.]6{10,}7"));

    // see: applyTestInvalid3()
    // assert_template_result "Liquid error: divided by 0", "{{ 5 | divided_by:0 }}"
  }
}
