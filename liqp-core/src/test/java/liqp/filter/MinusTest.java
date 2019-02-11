package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.LiquidParser;
import liqp.Mocks;
import liqp.LiquidTemplate;
import org.antlr.runtime.RecognitionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MinusTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ 8 | minus: 2 }}", "6"},
          {"{{ 8 | minus: 3 }}", "5"},
          {"{{ 8 | minus: 3. }}", "5.0"},
          {"{{ 8 | minus: 3.0 }}", "5.0"},
          {"{{ 8 | minus: 2.0 }}", "6.0"},
    };

    for (String[] test : tests) {

      LiquidTemplate template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render());

      assertThat(rendered, is(test[1]));
    }
  }

  @Test
  public void applyTestInvalid1() {
    Assertions.assertThat(
          LiquidDefaults.getDefaultFilters().getFilter("minus").onFilterAction(Mocks.mockRenderContext(), 1))
          .isEqualTo(1);
  }

  @Test
  public void applyTestInvalid2() {
    Assertions.assertThat(
          LiquidDefaults.getDefaultFilters().getFilter("minus").onFilterAction(Mocks.mockRenderContext(), 1, 2, 3))
          .isEqualTo(-4L);
  }

  /*
   * def test_minus
   *   assert_template_result "4", "{{ input | minus:operand }}", 'input' => 5, 'operand' => 1
   *   assert_template_result "2.3", "{{ '4.3' | minus:'2' }}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    assertThat(LiquidParser.newInstance().parse("{{ input | minus:operand }}").render("{\"input\":5, \"operand\":1}")
          , is((Object) "4"));
    assertThat(LiquidParser.newInstance().parse("{{ '4.3' | minus:'2' }}").render(), is((Object) "2.3"));
  }
}
