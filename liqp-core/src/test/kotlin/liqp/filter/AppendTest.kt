package liqp.filter

import liqp.LiquidParser
import liqp.parameterized.LiquifyNoInputTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runners.Parameterized

class AppendTests(template: String, expected: String) : LiquifyNoInputTest(template, expected) {

  companion object {
    @JvmStatic @Parameterized.Parameters fun testParams() =
        arrayOf(arrayOf("{{ 'a' | append: 'b' }}", "ab"),
            arrayOf("{{ '' | append: '' }}", ""),
            arrayOf("{{ 1 | append: 23 }}", "123"),
            arrayOf("{{ nil | append: 'a' }}", "a"),
            arrayOf("{{ nil | append: nil }}", ""))
  }
}

class AppendTest {

  /*
   * def test_append
   *   assigns = {'a' => 'bc', 'b' => 'd' }
   *   assert_template_result('bcd',"{{ a | append: 'd'}}",assigns)
   *   assert_template_result('bcd',"{{ a | append: b}}",assigns)
   * end
   */
  @Test
  fun applyOriginalTest() {

    val assigns = "{\"a\":\"bc\", \"b\":\"d\" }"

    assertThat(LiquidParser.newInstance().parse("{{ a | append: 'd'}}").render(assigns), `is`("bcd"))
    assertThat(LiquidParser.newInstance().parse("{{ a | append: b}}").render(assigns), `is`("bcd"))
  }
}
