package liqp.filter;

import static liqp.LiquidDefaults.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.LiquidParser;
import liqp.Mocks;
import liqp.Template;
import liqp.exceptions.LiquidRenderingException;
import org.antlr.runtime.RecognitionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ModuloTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ 8 | modulo: 2 }}", "0"},
          {"{{ 8 | modulo: 3 }}", "2"},
          {"{{ \"8\" | modulo: 3. }}", "2"},
          {"{{ 8 | modulo: 3.0 }}", "2"},
          {"{{ 8 | modulo: '2.0' }}", "0"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render());

      assertThat(rendered, is(test[1]));
    }
  }

  @Test(expected = LiquidRenderingException.class)
  public void applyTestInvalid1() {
    Assertions.assertThat(
          getDefaultFilters().getFilter("modulo").apply(Mocks.mockRenderContext(), 1))
          .isEqualTo(1L);
  }

  @Test
  public void applyTestInvalid2() {
    Assertions.assertThat(
          getDefaultFilters().getFilter("modulo").apply(Mocks.mockRenderContext(), 4, 2, 3))
          .isEqualTo(0);
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
