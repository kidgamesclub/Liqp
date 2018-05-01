package liqp.filter

import junitparams.JUnitParamsRunner
import liqp.LiquidDefaults
import liqp.Mocks.Companion.mockRenderContext
import liqp.parameterized.LiquifyNoInputTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class TruncatewordsTest : LiquifyNoInputTest("{ \"txt\" : \"a        b c d e f g h i j a b c d e f g h i j\" }") {

  override fun testParams(): Array<out Any> {
    return arrayOf(
        arrayOf("{{ nil | truncatewords }}", ""),
        arrayOf("{{ txt | truncatewords }}", "a b c d e f g h i j a b c d e..."),
        arrayOf("{{ txt | truncatewords: 5 }}", "a b c d e..."),
        arrayOf("{{ txt | truncatewords: 5, '???' }}", "a b c d e???"),
        arrayOf("{{ txt | truncatewords: 500, '???' }}", "a        b c d e f g h i j a b c d e f g h i j"),
        arrayOf("{{ txt | truncatewords: 2, '===' }}", "a b==="), arrayOf("{{ txt | truncatewords: 19, '===' }}", "a b c d e f g h i j a b c d e f g h i==="),
        arrayOf("{{ txt | truncatewords: 20, '===' }}", "a        b c d e f g h i j a b c d e f g h i j"),
        arrayOf("{{ txt | truncatewords: 21, '===' }}", "a        b c d e f g h i j a b c d e f g h i j"))
  }

  /*
   * def test_truncatewords
   *   assert_equal 'one two three', @filter.truncatewords('one two three', 4)
   *   assert_equal 'one two...', @filter.truncatewords('one two three', 2)
   *   assert_equal 'one two three', @filter.truncatewords('one two three')
   *   assert_equal 'Two small (13&#8221; x 5.5&#8221; x 10&#8221; high) baskets fit inside one large basket
   *   (13&#8221;...',
   *                 @filter.truncatewords('Two small (13&#8221; x 5.5&#8221; x 10&#8221; high) baskets fit inside
   *                 one large basket (13&#8221; x 16&#8221; x 10.5&#8221; high) with cover.', 15)
   * end
   */
  @Test
  fun applyOriginalTest() {

    val filter = LiquidDefaults.getDefaultFilters().getFilter<LFilter>("truncatewords")

    assertThat<Any>(filter.onFilterAction(mockRenderContext(), "one two three", ResolvedFilterParams(4)), `is`("one two three" as Any))
    assertThat<Any>(filter.onFilterAction(mockRenderContext(), "one two three", ResolvedFilterParams(2)), `is`("one two..." as Any))
    assertThat<Any>(filter.onFilterAction(mockRenderContext(), "one two three", ResolvedFilterParams(3)), `is`("one two three" as Any))
    assertThat<Any>(filter.onFilterAction(mockRenderContext(), "Two small (13&#8221; x 5.5&#8221; x 10&#8221; high) baskets " +
        "fit " +
        "inside one large basket (13&#8221; x 16&#8221; x 10.5&#8221; high) with cover.", ResolvedFilterParams(15)),
        `is`(("Two small (13&#8221; x 5.5&#8221; x 10&#8221; high) baskets fit inside one large basket " as Any).toString() + "(13&#8221;..."))
  }
}
