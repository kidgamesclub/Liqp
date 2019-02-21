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

public class RemoveFilterFirstFilterTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ '' | remove_first:'a' }}", ""},
          {"{{ nil | remove_first:'a' }}", ""},
          {"{{ 'aabbabc' | remove_first:'ab' }}", "ababc"},
          {"{{ 'ababab' | remove_first:'a' }}", "babab"},
    };

    for (String[] test : tests) {

      LTemplate template = createTestParser().parse(test[0]);
      String rendered = template.render();

      assertThat(rendered, is(test[1]));
    }
  }

  @Test(expected = RuntimeException.class)
  public void applyTestInvalidPattern() throws RecognitionException {
    createTestParser().parse("{{ 'ababab' | remove_first:nil }}").render();
  }

  /*
   * def test_remove
   *   assert_equal '   ', @filter.remove("a a a a", 'a')
   *   assert_equal 'a a a', @filter.remove_first("a a a a", 'a ')
   *   assert_template_result 'a a a', "{{ 'a a a a' | remove_first: 'a ' }}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("remove_first");

    assertThat(filter.onFilterAction(mockRenderContext(), "a a a a", "a "), is((Object) "a a a"));
    assertThat(createTestParser().parse("{{ 'a a a a' | remove_first: 'a ' }}")
          .render(), is((Object) "a a " +
          "a"));
  }
}
