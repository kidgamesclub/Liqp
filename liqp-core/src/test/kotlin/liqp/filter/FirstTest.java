package liqp.filter;

import static liqp.AssertsKt.createTestParser;
import static liqp.Mocks.mockRenderContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableList;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import liqp.params.FilterParams;
import liqp.params.ResolvedFilterParams;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class FirstTest {

  @Test
  public void applyTest() throws RecognitionException {

    LTemplate template = createTestParser().parse("{{values | first}}");

    String rendered = template.renderJson("{\"values\" : [\"Mu\", \"foo\", \"bar\"]}");

    assertThat(rendered, is("Mu"));
  }

  /*
   * def test_first_last
   *   assert_equal 1, @filter.first([1,2,3])
   *   assert_equal 3, @filter.last([1,2,3])
   *   assert_equal nil, @filter.first([])
   *   assert_equal nil, @filter.last([])
   * end
   */
  @Test
  public void applyOriginalTest() {

    final First filter = new First();

    final FilterParams params = new ResolvedFilterParams();
    assertThat(filter.onFilterAction(mockRenderContext(), ImmutableList.of(1, 2, 3), params), is(1));
    assertThat(filter.onFilterAction(mockRenderContext(), new Integer[]{}, params), is((Object) null));
  }
}