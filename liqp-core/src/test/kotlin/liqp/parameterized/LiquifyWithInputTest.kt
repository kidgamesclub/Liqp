package liqp.parameterized

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
abstract class LiquifyWithInputTest(val templateString: String, val expectedResult: String?, val inputData: Any? = null) {
  @Test
  open fun run() {
    val template = LiquidParser.newInstance().parse(templateString)
    val rendered = template.render(inputData)
    assert(rendered).isEqualTo(expectedResult)
  }
}
