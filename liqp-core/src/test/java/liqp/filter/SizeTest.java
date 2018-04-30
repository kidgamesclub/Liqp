package liqp.filter;

import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LiquidDefaults;
import liqp.Template;
import liqp.LiquidParser;
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

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render(json));

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
    assertThat(filter.doPostFilter(mockRenderContext(), new Integer[]{1, 2, 3}), is((Object) 3));
    assertThat(filter.doPostFilter(mockRenderContext(), new Object[0]), is((Object) 0));
    assertThat(filter.doPostFilter(mockRenderContext(), null), is((Object) 0));
  }
}
