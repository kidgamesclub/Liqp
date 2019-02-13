package liqp

import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StrictLogicTestIsTrue(val input:Any?, val expected:Boolean) {

  @Test
  fun testIsTrue() {
    val found = strictLogic.isTrue(input)
    Assertions.assertThat(found).isEqualTo(expected)
  }

  companion object {
    @JvmStatic   @Parameterized.Parameters(name = "{1}")
     fun isTrueParams(): Array<out Any> {
      return arrayOf(
          arrayOf(true, true),
          arrayOf(null, false),
          arrayOf(1, true)
      )
    }
  }
}

@RunWith(Parameterized::class)
class StrictLogicTestIsFalse(val input: Any?, val expected: Boolean) {
  @Test
  fun testIsFalse() {
    val found = strictLogic.isFalse(input)
    Assertions.assertThat(found).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameterized.Parameters
    fun isFalseParams(): Array<out Any> {
      return arrayOf(
          arrayOf(true, false),
          arrayOf(false, true),
          arrayOf(null, true),
          arrayOf(1, false)
      )
    }
  }
}
