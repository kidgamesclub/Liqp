package liqp.filter;

import static liqp.LiquidDefaults.getDefaultFilters;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidParser;
import liqp.Mocks;
import liqp.exceptions.LiquidRenderingException;
import liqp.parameterized.LiquifyNoInputTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ModuloTest extends LiquifyNoInputTest {

  public Object[] testParams() {

    String[][] tests = {
          {"{{ 8 | modulo: 2 }}", "0"},
          {"{{ 8 | modulo: 3 }}", "2"},
          {"{{ \"8\" | modulo: 3. }}", "2"},
          {"{{ 8 | modulo: 3.0 }}", "2.0"},
          {"{{ 8 | modulo: '2.0' }}", "0"},
    };

    return tests;
  }

  @Test(expected = LiquidRenderingException.class)
  public void applyTestInvalid1() {
    Assertions.assertThat(
          getDefaultFilters().getFilter("modulo").onFilterAction(Mocks.mockRenderContext(), 1))
          .isEqualTo(1L);
  }

  @Test
  public void applyTestInvalid2() {
    Assertions.assertThat(
          getDefaultFilters().getFilter("modulo").onFilterAction(Mocks.mockRenderContext(), 4, 2, 3))
          .isEqualTo(0L);
  }

  /*
   * def test_modulo
   *   assert_template_result "1", "{{ 3 | modulo:2 }}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    assertThat(LiquidParser.newInstance().parse("{{ 3 | modulo:2 }}").render(), is((Object) "1"));
  }
}
