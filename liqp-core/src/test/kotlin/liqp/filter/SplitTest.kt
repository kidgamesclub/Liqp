package liqp.filter

import liqp.parameterized.LiquifyNoInputTest
import liqp.assertThat
import org.junit.Test
import java.util.regex.Pattern

class SplitTest : LiquifyNoInputTest() {

  override fun testParams() = arrayOf(
      arrayOf("{{ 'a-b-c' | split:'-' }}", "abc"),
      arrayOf("{{ 'a-b-c' | split:'' }}", "a-b-c"),
      arrayOf("{{ 'a-b-c' | split:'?' }}", "a-b-c"),
      arrayOf("{{ 'a-b-c' | split:nil }}", "a-b-c"))

  /*
   * def test_strip
   *   assert_equal ['12','34'], @filter.split('12~34', '~')
   *   assert_equal ['A? ',' ,Z'], @filter.split('A? ~ ~ ~ ,Z', '~ ~ ~')
   *   assert_equal ['A?Z'], @filter.split('A?Z', '~')
   *   # Regexp works although Liquid does not support.
   *   assert_equal ['A','Z'], @filter.split('AxZ', /x/)
   * end
   */
  @Test
  fun testSimple() {

    Split().assertThat()
        .filtering("12~34", "~")
        .asList()
        .contains("12", "34")
  }

  @Test
  fun testBlankValues() {
    Split().assertThat()
        .filtering("A? ~ ~ ~ ,Z", "~ ~ ~")
        .asList()
        .contains("A? ", " ,Z")
  }

  @Test
  fun testNoSplit() {
    Split().assertThat()
        .filtering("A?Z", "~")
        .asList()
        .contains("A?Z")
  }

  @Test
  fun testSplitX() {
    Split().assertThat()
        .filtering("AxZ", Pattern.compile("x"))
        .asList()
        .contains("A", "Z")
  }
}
