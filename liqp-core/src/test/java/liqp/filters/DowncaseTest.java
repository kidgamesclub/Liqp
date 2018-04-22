package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Mocks;
import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class DowncaseTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ '' | downcase }}", ""},
          {"{{ nil | downcase }}", ""},
          {"{{ 'Abc' | downcase }}", "abc"},
          {"{{ 'abc' | downcase }}", "abc"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render());

      assertThat(rendered, is(test[1]));
    }
  }

  /*
   * def test_downcase
   *   assert_equal 'testing', @filters.downcase("Testing")
   *   assert_equal '', @filters.downcase(nil)
   * end
   */
  @Test
  public void applyOriginalTest() {

    final Filter filter = Filter.getFilter("downcase");
    assertThat(filter.apply(Mocks.mockRenderContext(), "Testing"), is((Object) "testing"));
    assertThat(filter.apply(Mocks.mockRenderContext(), null), is((Object) ""));
  }
}
