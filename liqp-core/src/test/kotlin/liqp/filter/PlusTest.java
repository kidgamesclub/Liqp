package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.LiquidParser;
import liqp.Mocks;
import liqp.LiquidTemplate;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PlusTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ 8 | plus: 2 }}", "10"},
          {"{{ 8 | plus: 3 }}", "11"},
          {"{{ 8 | plus: '3.' }}", "11.0"},
          {"{{ 8 | plus: 3.0 }}", "11.0"},
          {"{{ 8 | plus: \"2.0\" }}", "10.0"},
    };

    for (String[] test : tests) {

      LTemplate template = createTestParser().parse(test[0]);
      String rendered = template.render();

      assertThat(rendered, is(test[1]));
    }
  }

  @Test
  public void applyTestInvalid1() {
    Assertions.assertThat(LiquidDefaults.getDefaultFilters().getFilter("plus").onFilterAction(Mocks.mockRenderContext(), 1))
          .isEqualTo(1);
  }

  @Test
  public void applyTestInvalid2() {
    Assertions.assertThat(LiquidDefaults.getDefaultFilters().getFilter("plus").onFilterAction(Mocks.mockRenderContext(), 1, 2,
          3)).isEqualTo(6L);
  }

  /*
   * def test_plus
   *   assert_template_result "2", "{{ 1 | plus:1 }}"
   *   assert_template_result "2.0", "{{ '1' | plus:'1.0' }}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    assertThat(createTestParser().parse("{{ 1 | plus:1 }}").render(), is((Object) "2"));
    assertThat(createTestParser().parse("{{ '1' | plus:'1.0' }}").render(), is((Object) "2.0"));
  }
}
