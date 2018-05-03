package liqp

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class StrictLogicTest {

  @Test
  @Parameters(method = "isTrueParams")
  fun testIsTrue(input: Any?, expected: Boolean) {
    val found = strictLogic.isTrue(input)
    Assertions.assertThat(found).isEqualTo(expected)
  }

  fun isTrueParams(): Array<out Any> {
    return arrayOf(
        arrayOf(true, true),
        arrayOf(null, false),
        arrayOf(1, true)
    )
  }

  @Test
  @Parameters(method = "isFalseParams")
  fun testIsFalse(input: Any?, expected: Boolean) {
    val found = strictLogic.isFalse(input)
    Assertions.assertThat(found).isEqualTo(expected)
  }

  fun isFalseParams(): Array<out Any> {
    return arrayOf(
        arrayOf(true, false),
        arrayOf(false, true),
        arrayOf(null, true),
        arrayOf(1, false)
    )
  }
}
