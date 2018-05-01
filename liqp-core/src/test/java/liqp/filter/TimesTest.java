package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import junitparams.JUnitParamsRunner;
import liqp.AssertsKt;
import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TimesTest extends LiquifyNoInputTest {

  @Override
  public Object[] testParams() {
    String[][] tests = {
          {"{{ 8 | times: 2 }}", "16"},
          {"{{ 8 | times: 3 }}", "24"},
          {"{{ 8 | times: 3. }}", "24.0"},
          {"{{ 8 | times: '3.0' }}", "24.0"},
          {"{{ 8 | times: 2.0 }}", "16.0"},
          {"{{ foo | times: 4 }}", "0"},
    };

    return tests;
  }

  @Test
  public void applyTestInvalid1() {
    AssertsKt.assertThat(new Times())
          .filtering(1)
          .hadNoErrors()
          .isEqualTo(1);
  }

  @Test
  public void applyTestInvalid2() {
    AssertsKt.assertThat(new Times())
          .filtering(1, 2, 3)
          .hadNoErrors()
          .isEqualTo(6L);
  }

  /*
   * def test_times
   *   assert_template_result "12", "{{ 3 | times:4 }}"
   *   assert_template_result "0", "{{ 'foo' | times:4 }}"
   *
   *   # Ruby v1.9.2-rc1, or higher, backwards compatible Float test
   *   assert_match(/(6\.3)|(6\.(0{13})1)/, TemplateFactory.newInstance().parse("{{ '2.1' | times:3 }}").render)
   *
   *   assert_template_result "6", "{{ '2.1' | times:3 | replace: '.','-' | plus:0}}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("times");

    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), 3L, 4L), is((Object) 12L));
    // assert_template_result "0", "{{ 'foo' | times:4 }}" // see: applyTest()
    assertTrue(String.valueOf(filter.onFilterAction(Mocks.mockRenderContext(), 2.1, 3L)).matches("6[.,]30{10,}1"));
  }
}
