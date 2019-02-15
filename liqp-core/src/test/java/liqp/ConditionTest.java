package liqp;

import static liqp.TestUtils.getNode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.ZoneId;
import java.util.Locale;
import liqp.context.LContext;
import liqp.nodes.RenderContext;
import org.junit.Before;
import org.junit.Test;

public class ConditionTest {
  private LRenderer engine;
  private RenderContext context;

  @Before
  public void setUp() {
    LParser parser = Liquify.getProvider().createParser();
    engine = Liquify.getProvider().createRenderer(parser, Liquify.getProvider().getDefaultRenderSettings());
    context = Mocks.mockRenderContext();
  }

  /*
   * def test_basic_condition
   *   assert_equal false, Condition.new('1', '==', '2').evaluate
   *   assert_equal true,  Condition.new('1', '==', '1').evaluate
   * end
   */
  @Test
  public void basicConditionTest() throws Exception {

    assertThat(getNode("1 == 2").invoke(context, Boolean.class), is(false));
    assertThat(getNode("1 == 1").invoke(context, Boolean.class), is(true));
  }

  /*
   * def test_default_operators_evalute_true
   *   assert_evalutes_true '1', '==', '1'
   *   assert_evalutes_true '1', '!=', '2'
   *   assert_evalutes_true '1', '<>', '2'
   *   assert_evalutes_true '1', '<', '2'
   *   assert_evalutes_true '2', '>', '1'
   *   assert_evalutes_true '1', '>=', '1'
   *   assert_evalutes_true '2', '>=', '1'
   *   assert_evalutes_true '1', '<=', '2'
   *   assert_evalutes_true '1', '<=', '1'
   *   # negative numbers
   *   assert_evalutes_true '1', '>', '-1'
   *   assert_evalutes_true '-1', '<', '1'
   *   assert_evalutes_true '1.0', '>', '-1.0'
   *   assert_evalutes_true '-1.0', '<', '1.0'
   * end
   */
  @Test
  public void defaultOperatorsEvaluteTrueTest() throws Exception {

    assertThat(getNode("1 == 1").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1 != 2").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1 <> 2").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1 < 2").invoke(context, Boolean.class), is(true));
    assertThat(getNode("2 > 1").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1 >= 1").invoke(context, Boolean.class), is(true));
    assertThat(getNode("2 >= 1").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1 <= 2").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1 <= 1").invoke(context, Boolean.class), is(true));

    // negative numbers
    assertThat(getNode("1 > -1").invoke(context, Boolean.class), is(true));
    assertThat(getNode("-1 < 1").invoke(context, Boolean.class), is(true));
    assertThat(getNode("1.0 > -1.0").invoke(context, Boolean.class), is(true));
    assertThat(getNode("-1.0 < 1.0").invoke(context, Boolean.class), is(true));
  }

  /*
   * def test_default_operators_evalute_false
   *   assert_evalutes_false '1', '==', '2'
   *   assert_evalutes_false '1', '!=', '1'
   *   assert_evalutes_false '1', '<>', '1'
   *   assert_evalutes_false '1', '<', '0'
   *   assert_evalutes_false '2', '>', '4'
   *   assert_evalutes_false '1', '>=', '3'
   *   assert_evalutes_false '2', '>=', '4'
   *   assert_evalutes_false '1', '<=', '0'
   * end
   */
  @Test
  public void defaultOperatorsEvaluateFalseTest() throws Exception {

    final RenderContext context = Mocks.mockRenderContext();
    assertThat(getNode("1 == 2").invoke(context, Boolean.class), is(false));
    assertThat(getNode("1 != 1").invoke(context, Boolean.class), is(false));
    assertThat(getNode("1 <> 1").invoke(context, Boolean.class), is(false));
    assertThat(getNode("1 < 0").invoke(context, Boolean.class), is(false));
    assertThat(getNode("2 > 4").invoke(context, Boolean.class), is(false));
    assertThat(getNode("1 >= 3").invoke(context, Boolean.class), is(false));
    assertThat(getNode("2 >= 4").invoke(context, Boolean.class), is(false));
    assertThat(getNode("1 <= 0").invoke(context, Boolean.class), is(false));
  }

  /*
   * def test_contains_works_on_strings
   *   assert_evalutes_true "'bob'", 'contains', "'o'"
   *   assert_evalutes_true "'bob'", 'contains', "'b'"
   *   assert_evalutes_true "'bob'", 'contains', "'bo'"
   *   assert_evalutes_true "'bob'", 'contains', "'ob'"
   *   assert_evalutes_true "'bob'", 'contains', "'bob'"
   *
   *   assert_evalutes_false "'bob'", 'contains', "'bob2'"
   *   assert_evalutes_false "'bob'", 'contains', "'a'"
   *   assert_evalutes_false "'bob'", 'contains', "'---'"
   * end
   */
  @Test
  public void containsWorksOnStringsTest() throws Exception {

    assertThat(getNode("'bob' contains 'o'").render(context), is((Object) true));
    assertThat(getNode("'bob' contains 'b'").render(context), is((Object) true));
    assertThat(getNode("'bob' contains 'bo'").render(context), is((Object) true));
    assertThat(getNode("'bob' contains 'ob'").render(context), is((Object) true));
    assertThat(getNode("'bob' contains 'bob'").render(context), is((Object) true));

    assertThat(getNode("'bob' contains 'bob2'").render(context), is((Object) false));
    assertThat(getNode("'bob' contains 'a'").render(context), is((Object) false));
    assertThat(getNode("'bob' contains '---'").render(context), is((Object) false));
  }

  /*
   * def test_contains_works_on_arrays
   *   @context = Liquid::Context.new
   *   @context['array'] = [1,2,3,4,5]
   *
   *   assert_evalutes_false "array",  'contains', '0'
   *   assert_evalutes_true "array",   'contains', '1'
   *   assert_evalutes_true "array",   'contains', '2'
   *   assert_evalutes_true "array",   'contains', '3'
   *   assert_evalutes_true "array",   'contains', '4'
   *   assert_evalutes_true "array",   'contains', '5'
   *   assert_evalutes_false "array",  'contains', '6'
   *   assert_evalutes_false "array",  'contains', '"1"'
   * end
   */
  @Test
  public void containsWorksOnArraysTest() throws Exception {

    context.set("array", new Long[]{1L, 2L, 3L, 4L, 5L});

    assertThat(getNode("array contains 0").render(context), is((Object) false));
    assertThat(getNode("array contains 1").render(context), is((Object) true));
    assertThat(getNode("array contains 2").render(context), is((Object) true));
    assertThat(getNode("array contains 3").render(context), is((Object) true));
    assertThat(getNode("array contains 4").render(context), is((Object) true));
    assertThat(getNode("array contains 5").render(context), is((Object) true));
    assertThat(getNode("array contains 6").render(context), is((Object) false));
    assertThat(getNode("array contains '1'").render(context), is((Object) false));
  }

  /*
   * def test_contains_returns_false_for_nil_operands
   *   @context = Liquid::Context.new
   *   assert_evalutes_false "not_assigned", 'contains', '0'
   *   assert_evalutes_false "0", 'contains', 'not_assigned'
   * end
   */
  @Test
  public void containsReturnsFalseForNilOperandsTest() throws Exception {

    assertThat(getNode("not_assigned contains 0").render(context), is((Object) false));
    assertThat(getNode("0 contains not_assigned").render(context), is((Object) false));
  }

  /*
   * def test_left_or_right_may_contain_operators
   *   @context = Liquid::Context.new
   *   @context['one'] = @context['another'] = "gnomeslab-and-or-liquid"
   *
   *   assert_evalutes_true "one", '==', "another"
   * end
   */
  @Test
  public void leftOrRightMayContainOperatorsTest() throws Exception {

    context.set("one", "gnomeslab-and-or-liquid");
    context.set("another", "gnomeslab-and-or-liquid");

    assertThat(getNode("one == another").invoke(context, Boolean.class), is(true));
  }

  private LContext createTestContext() {
    return engine.createRenderContext(Locale.US, ZoneId.systemDefault(), context);
  }
}
