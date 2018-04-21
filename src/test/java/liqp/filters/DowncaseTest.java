package liqp.filters;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import liqp.Template;
import liqp.TemplateEngine;
import liqp.TemplateFactory;
import liqp.nodes.RenderContext;
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

      Template template = TemplateFactory.newBuilder().parse(test[0]);
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
    final RenderContext context = new RenderContext(emptyMap(),
          mock(TemplateFactory.class),
          mock(TemplateEngine.class));
    assertThat(filter.apply(context, "Testing"), is((Object) "testing"));
    assertThat(filter.apply(context, null), is((Object) ""));
  }
}
