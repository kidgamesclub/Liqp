package liqp

import assertk.assertAll
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.Test

class TruthKtTest {
  @Test fun testTruthy_ListOfEmptyStrings() {
    val values = listOf("  ", "", " ")
    assertAll {
      assertk.assertThat(values.isFalsy(), "falsy").isTrue()
      assertk.assertThat(values.isTruthy(), "truthy").isFalse()
    }
  }

  @Test fun testTruthy_BlankString() {
    val values = "  "
    assertAll {
      assertk.assertThat(values.isFalsy(), "falsy").isTrue()
      assertk.assertThat(values.isTruthy(), "truthy").isFalse()
    }
  }

  @Test fun testTruthy_EmptyString() {
    val values = ""
    assertAll {
      assertk.assertThat(values.isFalsy(), "falsy").isTrue()
      assertk.assertThat(values.isTruthy(), "truthy").isFalse()
    }
  }

  @Test fun testTruthy_NonEmptyString() {
    val values = "a"
    assertAll {
      assertk.assertThat(values.isFalsy(), "falsy").isFalse()
      assertk.assertThat(values.isTruthy(), "truthy").isTrue()
    }
  }
}
