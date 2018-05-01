package liqp.filter;

import static liqp.LiquidParser.newInstance;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.LiquidDefaults;
import liqp.Mocks;
import liqp.parameterized.LiquifyNoInputTest;
import org.antlr.runtime.RecognitionException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ReplaceFirstTest extends LiquifyNoInputTest {

  public Object[] testParams() {
    return new String[][]{
          {"{{ '' | replace_first:'a', 'A' }}", ""},
          {"{{ nil | replace_first:'a', 'A' }}", ""},
          {"{{ 'aabbabab' | replace_first:'ab', 'A' }}", "aAbabab"},
          {"{{ 'ababab' | replace_first:'a', 'A' }}", "Ababab"},
    };
  }

  @Test
  public void applyTestInvalidPattern1() throws RecognitionException {
    final String render = newInstance().parse("{{ 'ababab' | replace_first:nil, 'A' }}").render();
    Assertions.assertThat(render).isEqualTo("ababab");
  }

  @Test
  public void applyTestInvalidPattern2() throws RecognitionException {
    final String render = newInstance().parse("{{ 'ababab' | replace_first:'a', nil }}").render();
    Assertions.assertThat(render).isEqualTo("babab");
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

    LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("replace_first");

    assertThat(filter.onFilterAction(Mocks.mockRenderContext(), "a a a a", "a", "b"), is((Object) "b a a a"));
    assertThat(newInstance().parse("{{ 'a a a a' | replace_first: 'a', 'b' }}").render(), is("b a a a"));
  }
}
