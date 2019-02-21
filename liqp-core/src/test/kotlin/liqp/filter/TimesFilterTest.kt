package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.matches
import liqp.LiquidDefaults
import liqp.assertThat
import liqp.parameterized.LiquifyWithInputTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class TimesFilterTestParameterized(templateString: String, expectedResult: String?)
  : LiquifyWithInputTest(templateString, expectedResult) {

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{0}={1}")
    fun testParams() =
        arrayOf(
            arrayOf("{{ 8 | times: 2 }}", "16"),
            arrayOf("{{ 8 | times: 3 }}", "24"),
            arrayOf("{{ 8 | times: 3. }}", "24.0"),
            arrayOf("{{ 8 | times: '3.0' }}", "24.0"),
            arrayOf("{{ 8 | times: 2.0 }}", "16.0"),
            arrayOf("{{ foo | times: 4 }}", ""))
  }
}

class TimesFilterTest {
  @Test
  fun applyTestInvalid1() {
    TimesFilter().assertThat()
        .filtering(1)
        .hadNoErrors()
        .isEqualTo(1)
  }

  @Test
  fun applyTestInvalid2() {
    TimesFilter().assertThat()
        .filtering(1, 2, 3)
        .hadNoErrors()
        .isEqualTo(6L)
  }

  /*
          * def test_times
          *   assert_template_result "12", "{{ 3 | times:4 }}"
          *   assert_template_result "0", "{{ 'foo' | times:4 }}"
          *
          *   # Ruby v1.9.2-rc1, or higher, backwards compatible Float test
          *   assert_match(/(6\.3)|(6\.(0{13})1)/, TemplateFactory.newInstance().parse("{{ '2.1' | times:3 }}").render)
          *
          *   assert_template_result "6", "{{ '2.1' | times:3 | replace: '.','-' | plus:0}}"
          * end
          */
  @Test
  fun applyOriginalTest() {

    val filter = LiquidDefaults.defaultFilters.getFilter<LFilter>("times")

    assert(filter.onFilterAction(context, 3L, 4L)).isEqualTo(12L as Any)
    // assert_template_result "0", "{{ 'foo' | times:4 }}" // see: applyTest()
    assert(filter.onFilterAction(context, 2.1, 3L).toString()).matches("6[.,]30{10,}1".toRegex())
  }
}
