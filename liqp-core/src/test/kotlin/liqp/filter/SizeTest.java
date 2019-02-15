package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class SizeTest {

  @Test
  public void applyTest() throws RecognitionException {

    String json = "{ \"n\" : [1,2,3,4,5] }";

    String[][] tests = {
          {"{{ nil | size }}", "0"},
          {"{{ 999999999999999 | size }}", "8"},
          {"{{ '1' | size }}", "1"},
          {"{{ N | size }}", "0"},
          {"{{ n | size }}", "5"},
          {"{{ true | size }}", "1"},
    };

    for (String[] test : tests) {

      LTemplate template = createTestParser().parse(test[0]);
      String rendered = template.renderJson(json);

      assertThat(rendered, is(test[1]));
    }
  }

  /*
   * def test_size
   *   assert_equal 3, @filter.size([1,2,3])
   *   assert_equal 0, @filter.size([])
   *   assert_equal 0, @filter.size(nil)
   * end
   */
  @Test
  public void applyOriginalTest() {

    final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("size");
    assertThat(filter.onFilterAction(mockRenderContext(), new Integer[]{1, 2, 3}), is((Object) 3));
    assertThat(filter.onFilterAction(mockRenderContext(), new Object[0]), is((Object) 0));
    assertThat(filter.onFilterAction(mockRenderContext(), null), is((Object) 0));
  }
}
