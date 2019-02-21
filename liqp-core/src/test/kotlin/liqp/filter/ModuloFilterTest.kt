package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidDefaults.defaultFilters
import liqp.LiquidParser
import liqp.Mocks
import liqp.createTestParser
import liqp.exceptions.LiquidRenderingException
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

private val modulo = defaultFilters["modulo"]
val context = Mocks.mockRenderContext()

@RunWith(Parameterized::class)
class ModuloFilterTestParameterized(val template: String, val expected: String) {

  @Test
  fun run() {
    val template = createTestParser {  }.parse(template)
    val rendered = template.render()
    assert(rendered).isEqualTo(expected)
  }

  companion object {
    @JvmStatic
    @Parameterized.Parameters(name = "{0}={1}")
    fun testParams() = arrayOf(
        arrayOf("{{ 8 | modulo: 2 }}", "0"),
        arrayOf("{{ 8 | modulo: 3 }}", "2"),
        arrayOf("{{ \"8\" | modulo: 3. }}", "2.0"),
        arrayOf("{{ 8 | modulo: 3.0 }}", "2.0"),
        arrayOf("{{ 8 | modulo: '2.0' }}", "0.0"))
  }
}

class ModuloFilterTest {
  @Test(expected = LiquidRenderingException::class)
  fun invalid1() {
    Assertions.assertThat(
        modulo.onFilterAction(context, 1))
        .isEqualTo(1L)
  }

  @Test
  fun invalid2() {
    assert(modulo.onFilterAction(context, 4, 2, 3))
        .isEqualTo(0L)
  }

  /*
   * def test_modulo
   *   assert_template_result "1", "{{ 3 | modulo:2 }}"
   * end
   */
  @Test
  fun applyOriginalTest() {
    assert(createTestParser {  }.parse("{{ 3 | modulo:2 }}").render()).isEqualTo("1")
  }
}
