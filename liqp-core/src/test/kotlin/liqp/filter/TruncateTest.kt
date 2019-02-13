package liqp.filter

import liqp.assertThat
import liqp.parameterized.LiquifyNoInputTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class TruncateTestParameterized(templateString: String, expectedResult: String) : LiquifyNoInputTest(templateString,
    expectedResult,
    "{ \"txt\" : \"012345678901234567890123456789012345678901234567890123456789\" }") {

  companion object {
    @JvmStatic @Parameters(name = "{0}={1}")
    fun params() = arrayOf(arrayOf("{{ nil | truncate }}", ""),
        arrayOf("{{ txt | truncate }}", "01234567890123456789012345678901234567890123456..."),
        arrayOf("{{ txt | truncate: 5 }}", "01..."),
        arrayOf("{{ txt | truncate: 5, '???' }}", "01???"),
        arrayOf("{{ txt | truncate: 500, '???' }}", "012345678901234567890123456789012345678901234567890123456789"),
        arrayOf("{{ txt | truncate: 2, '===' }}", "==="),
        arrayOf("{{ '12345' | truncate: 4, '===' }}", "1==="))
  }
}

class TruncateTest {
  @Test
  fun truncateNumbers() {
    Truncate().assertThat()
        .filtering("1234567890")
        .withParams(7)
        .isEqualTo("1234...")
  }

  @Test
  fun truncateNumbers_Params() {
    Truncate().assertThat()
        .filtering("1234567890", 20)
        .isEqualTo("1234567890")
  }

  @Test
  fun truncateNumbers_Empty() {
    Truncate().assertThat()
        .filtering("1234567890", 0)
        .isEqualTo("...")
  }

  @Test
  fun truncateNumbers_Full() {
    Truncate().assertThat()
        .filtering("1234567890")
        .isEqualTo("1234567890")
  }
}
