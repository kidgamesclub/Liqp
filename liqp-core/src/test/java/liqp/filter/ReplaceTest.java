package liqp.filter;

import static liqp.LiquidDefaults.*;
import static liqp.LiquidParser.newInstance;
import static liqp.Mocks.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import liqp.params.FilterParams;
import org.antlr.runtime.RecognitionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ReplaceTest {

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ '' | replace:'a', 'A' }}", ""},
          {"{{ nil | replace:'a', 'A' }}", ""},
          {"{{ 'aabb' | replace:'ab', 'A' }}", "aAb"},
          {"{{ 'ababab' | replace:'a', 'A' }}", "AbAbAb"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render());

      assertThat(rendered, is(test[1]));
    }
  }

  @Test
  public void applyTestInvalidPattern1() throws RecognitionException {
    final String render = newInstance().parse("{{ 'ababab' | replace:nil, 'A' }}").render();
    Assertions.assertThat(render).isEqualTo("ababab");
  }

  @Test
  public void applyTestInvalidPattern2() throws RecognitionException {
    final String render = newInstance().parse("{{ 'ababab' | replace:'a', nil }}").render();
    Assertions.assertThat(render).isEqualTo("bbb");
  }

  /*
   * def test_replace
   *   assert_equal 'b b b b', @filter.replace("a a a a", 'a', 'b')
   *   assert_equal 'b a a a', @filter.replace_first("a a a a", 'a', 'b')
   *   assert_template_result 'b a a a', "{{ 'a a a a' | replace_first: 'a', 'b' }}"
   * end
   */
  @Test
  public void applyOriginalTest() {

    LFilter filter = getDefaultFilters().getFilter("replace");
    assertThat(filter.onFilterAction(mockRenderContext(), "a a a a", FilterParams.of("a", "b")), is("b b b b"));
  }
}
