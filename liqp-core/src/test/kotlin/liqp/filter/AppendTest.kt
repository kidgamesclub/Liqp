package liqp.filter

import liqp.LiquidParser
import liqp.createTestParser
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

    assertThat(createTestParser{}.parse("{{ a | append: 'd'}}").renderJson(assigns), `is`("bcd"))
    assertThat(createTestParser{}.parse("{{ a | append: b}}").renderJson(assigns), `is`("bcd"))
  }
}
