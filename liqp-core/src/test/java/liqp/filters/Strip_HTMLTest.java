package liqp.filters;

import static liqp.filters.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class Strip_HTMLTest {

  @Test
  public void applyTest() throws RecognitionException {

    String json = "{ \"html\" : \"1<h>2</h>3\" }";

    String[][] tests = {
          {"{{ nil | strip_html }}", ""},
          {"{{ 456 | strip_html }}", "456"},
          {"{{ '45<6' | strip_html }}", "45<6"},
          {"{{ '<a>' | strip_html }}", ""},
          {"{{ html | strip_html }}", "123"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render(json));

      assertThat(rendered, is(test[1]));
    }
  }

  /*
   * def test_strip_html
   *   assert_equal 'test', @filters.strip_html("<div>test</div>")
   *   assert_equal 'test', @filters.strip_html("<div id='test'>test</div>")
   *   assert_equal '', @filters.strip_html("<script type='text/javascript'>document.write('some stuff');</script>")
   *   assert_equal '', @filters.strip_html(nil)
   * end
   */
  @Test
  public void applyOriginalTest() {

    Filter filter = Filter.getFilter("strip_html");
    assertThat(filter.apply(mockRenderContext(), "<div>test</div>"), is((Object) "test"));
    assertThat(filter.apply(mockRenderContext(), "<div id='test'>test</div>"), is((Object) "test"));
    assertThat(filter.apply(mockRenderContext(), "<script type='text/javascript'>document.write('some stuff');" +
          "</script>"), is((Object) ""));
    assertThat(filter.apply(mockRenderContext(), null), is((Object) ""));
  }
}
