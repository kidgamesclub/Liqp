package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidDefaults
import liqp.LiquidDefaults.defaultFilters
import liqp.LiquidParser
import liqp.Mocks
import liqp.createTestParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
data class MinusFilterTest(val test: String, val expected: String) {
  val lcontext = Mocks.mockRenderContext()

  @Test
  fun minusTest() {
    val template = createTestParser {  }.parse(test)
    val rendered = template.render()

    assert(rendered).isEqualTo(expected)
  }

  @Test
  fun invalid1() {
    assert(defaultFilters["minus"].onFilterAction(lcontext, 1))
        .isEqualTo(1)
  }

  @Test
  fun invalid2() {
    assert(
        defaultFilters["minus"].onFilterAction(lcontext, 1, 2, 3))
        .isEqualTo(-4L)
  }

  /*
   * def test_minus
   *   assert_template_result "4", "{{ input | minus:operand }}", 'input' => 5, 'operand' => 1
   *   assert_template_result "2.3", "{{ '4.3' | minus:'2' }}"
   * end
   */
  @Test
  fun originalTest() {

    assert(createTestParser {  }.parse("{{ input | minus:operand }}")
        .renderJson("{\"input\":5, \"operand\":1}")).isEqualTo("4")
    assert(createTestParser {  }.parse("{{ '4.3' | minus:'2' }}")
        .render()).isEqualTo("2.3")
  }

  companion object {
    @Parameterized.Parameters(name = "{0}={1}")
    @JvmStatic
    fun tests(): Array<Array<String>> {
      return arrayOf(
          arrayOf("{{ 8 | minus: 2 }}", "6"),
          arrayOf("{{ 8 | minus: 3 }}", "5"),
          arrayOf("{{ 8 | minus: 3. }}", "5.0"),
          arrayOf("{{ 8 | minus: 3.0 }}", "5.0"),
          arrayOf("{{ 8 | minus: 3.5 }}", "4.5"),
          arrayOf("{{ 8.6 | minus: 3.5 }}", "5.1"),
          arrayOf("{{ 8 | minus: 2.0 }}", "6.0"))
    }
  }
}
