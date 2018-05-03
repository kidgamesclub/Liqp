package liqp.filter

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import liqp.assertThat
import liqp.parameterized.LiquifyNoInputTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class TruncatewordsTest : LiquifyNoInputTest("{ \"txt\" : \"a        b c d e f g h i j a b c d e f g h i j\" }") {

  override fun testParams(): Array<out Any> {
    return arrayOf(
        arrayOf("{{ nil | truncatewords }}", ""),
        arrayOf("{{ txt | truncatewords }}", "a b c d e f g h i j a b c d e..."),
        arrayOf("{{ txt | truncatewords: 5 }}", "a b c d e..."),
        arrayOf("{{ txt | truncatewords: 5, '???' }}", "a b c d e???"),
        arrayOf("{{ txt | truncatewords: 500, '???' }}", "a        b c d e f g h i j a b c d e f g h i j"),
        arrayOf("{{ txt | truncatewords: 2, '===' }}", "a b==="), arrayOf("{{ txt | truncatewords: 19, '===' }}", "a b c d e f g h i j a b c d e f g h i==="),
        arrayOf("{{ txt | truncatewords: 20, '===' }}", "a        b c d e f g h i j a b c d e f g h i j"),
        arrayOf("{{ txt | truncatewords: 21, '===' }}", "a        b c d e f g h i j a b c d e f g h i j"))
  }

  @Test
  @Parameters(method = "testParams")
  override fun run(templateString: String?, expectedResult: String?) {
    super.run(templateString, expectedResult)
  }

  @Test
  fun testTruncatedBounds() {
    Truncatewords().assertThat()
        .filtering(inputData = "one two three")
        .withParams(2)
        .isEqualTo("one two...")
  }

  @Test
  fun testEqualsBounds() {

    Truncatewords().assertThat()
        .filtering(inputData = "one two three")
        .withParams(3)
        .isEqualTo("one two three")
  }

  @Test
  fun testNoBoundParam() {
    Truncatewords().assertThat()
        .filtering(inputData = "one two three")
        .withParams()
        .isEqualTo("one two three")
  }

  @Test
  fun testWithinBounds() {
    Truncatewords().assertThat()
        .filtering(inputData = "one two three")
        .withParams(4).isEqualTo("one two three")
  }

  @Test
  fun testUnicodeEscapeCharacters() {
    val containsEscapes = "Two small (13&#8221; x 5.5&#8221; x 10&#8221; high) baskets " +
        "fit " +
        "inside one large basket (13&#8221; x 16&#8221; x 10.5&#8221; high) with cover."


    Truncatewords().assertThat()
        .filtering(containsEscapes)
        .withParams(15)
        .isEqualTo("Two small (13&#8221; x 5.5&#8221; x 10&#8221; high) baskets fit inside one large basket (13&#8221;...")
  }
}
