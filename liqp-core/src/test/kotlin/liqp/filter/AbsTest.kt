package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidParser
import liqp.createTestParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class AbsTest(val template: String, val expected: String) {

  @Test
  fun applyTest() {
    val template = createTestParser{}.parse(template)
    val rendered = template.render()
    assert(rendered).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameters(name = "{0} = {1}")
    fun tests() = arrayOf(
        arrayOf("{{ 17 | abs }}", "17"),
        arrayOf("{{ -17 | abs }}", "17"),
        arrayOf("{{ '17' | abs }}", "17"),
        arrayOf("{{ '-17' | abs }}", "17"),
        arrayOf("{{ 0 | abs }}", "0"),
        arrayOf("{{ '0' | abs }}", "0"),
        arrayOf("{{ 17.42 | abs }}", "17.42"),
        arrayOf("{{ -17.42 | abs }}", "17.42"),
        arrayOf("{{ '17.42' | abs }}", "17.42"),
        arrayOf("{{ '-17.42' | abs }}", "17.42"))
  }
}
