package liqp.filter;

import static liqp.LiquidDefaults.getDefaultFilters;
import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import liqp.LiquidParser;
import liqp.Template;
import liqp.exceptions.LiquidRenderingException;
import org.antlr.runtime.RecognitionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class Divided_ByTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ 8 | divided_by: 2 }}", "4"},
          {"{{ 8 | divided_by: 3 }}", "2"},
          {"{{ 8 | divided_by: 3. }}", String.valueOf(8 / 3.0)},
          {"{{ 8 | divided_by: 3.0 }}", String.valueOf(8 / 3.0)},
          {"{{ 8 | divided_by: 2.0 }}", "4.0"},
          {"{{ 0 | divided_by: 2.0 }}", "0.0"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render());

      assertThat(rendered, is(test[1]));
    }
  }

  @Test(expected = LiquidRenderingException.class)
  public void applyTestInvalid1() {
    getDefaultFilters().getFilter("divided_by").apply(mockRenderContext(), 1);
  }

  @Test
  public void applyTestInvalid2() {
    final LFilter filter = getDefaultFilters().getFilter("divided_by");
    Assertions.assertThat(filter.apply(mockRenderContext(), 1, 2, 3))
          .isEqualTo(0.5);
  }

  @Test(expected = LiquidRenderingException.class)
  public void applyTestInvalid3() {
    getDefaultFilters().getFilter("divided_by").apply(mockRenderContext(), 15L, 0L);
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

    assertThat(filter.doPostFilter(mockRenderContext(), 12L, 3L), is((Object) 4L));
    assertThat(filter.doPostFilter(mockRenderContext(), 14L, 3L), is((Object) 4L));
    assertTrue(String.valueOf(filter.doPostFilter(mockRenderContext(), 14L, 3.0)).matches("4[,.]6{10,}7"));

    // see: applyTestInvalid3()
    // assert_template_result "Liquid error: divided by 0", "{{ 5 | divided_by:0 }}"
  }
}
