package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.matches
import liqp.LiquidDefaults
import liqp.LiquidDefaults.defaultFilters
import liqp.Mocks.Companion.mockRenderContext
import liqp.exceptions.LiquidRenderingException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class DividedByTest {

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{0)={1}")
    fun testParams() =
        arrayOf(
            arrayOf("{{ 8 | divided_by: 2 }}", "4"),
            arrayOf("{{ 8 | divided_by: 3 }}", "2"),
            arrayOf("{{ 8 | divided_by: 3. }}", (8 / 3.0).toString()),
            arrayOf("{{ 8 | divided_by: 3.0 }}", (8 / 3.0).toString()),
            arrayOf("{{ 8 | divided_by: 2.0 }}", "4.0"),
            arrayOf("{{ 0 | divided_by: 2.0 }}", "0.0"))
  }

  @Test
  fun applyTestInvalid1() {
    assert(LiquidDefaults.defaultFilters
        .getFilter<LFilter>("divided_by")
        .onFilterAction(mockRenderContext(), 1))
        .isEqualTo(1)
  }

  @Test
  fun applyTestInvalid2() {
    val filter = defaultFilters.getFilter<LFilter>("divided_by")
    assert(filter.onFilterAction(mockRenderContext(), 1, 2.0, 3))
        .isEqualTo(1.0 / 6)
  }

  @Test(expected = LiquidRenderingException::class)
  fun applyTestInvalid3() {
    defaultFilters.getFilter<LFilter>("divided_by").onFilterAction(mockRenderContext(), 15L, 0L)
  }

  /*
   * def test_divided_by
   *   assert_template_result "4", "{{ 12 | divided_by:3 }}"
   *   assert_template_result "4", "{{ 14 | divided_by:3 }}"
   *
   *   # Ruby v1.9.2-rc1, or higher, backwards compatible Float test
   *   assert_match(/4\.(6{13,14})7/, TemplateFactory.newInstance().parse("{{ 14 | divided_by:'3.0' }}").render)
   *
   *   assert_template_result "5", "{{ 15 | divided_by:3 }}"
   *   assert_template_result "Liquid error: divided by 0", "{{ 5 | divided_by:0 }}"
   * end
   */
  @Test
  fun applyOriginalTest() {

    val filter = defaultFilters.getFilter<LFilter>("divided_by")

    assert(filter.onFilterAction(mockRenderContext(), 12L, 3L)).isEqualTo(4L)
    assert(filter.onFilterAction(mockRenderContext(), 14L, 3L)).isEqualTo(4L)
    assert(filter.onFilterAction(mockRenderContext(), 14L, 3.0).toString()).matches("4[,.]6{10,}7".toRegex())

    // see: applyTestInvalid3()
    // assert_template_result "Liquid error: divided by 0", "{{ 5 | divided_by:0 }}"
  }
}
