package liqp.parameterized

import liqp.LiquidParser
import liqp.createTestParser
import liqp.parseIfNecessary
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
abstract class LiquifyNoInputTest @JvmOverloads constructor(val templateString: String, val expectedResult: String, val inputData: String? = null) {
  @Test
  open fun run() {
    val template = createTestParser{}.parse(templateString)
    val input = try {
      inputData.parseIfNecessary()
    } catch (e: Exception) {
      inputData
    }
    val rendered = template.render(input)

    assertThat(rendered).isEqualTo(expectedResult)
  }
}
