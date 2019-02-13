package liqp.parameterized

import liqp.LiquidParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
abstract class LiquifyNoInputTest @JvmOverloads constructor(val templateString: String, val expectedResult: String, val input: String? = null) {
  @Test
  open fun run() {
    val template = LiquidParser.newInstance().parse(templateString)
    val rendered = template.render(input)

    assertThat(rendered).isEqualTo(expectedResult)
  }
}
