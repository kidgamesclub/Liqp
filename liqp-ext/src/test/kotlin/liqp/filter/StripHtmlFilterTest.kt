package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidParser
import liqp.assertThat
import liqp.ext.filters.strings.StripHtmlFilter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StripHtmlFilterTestParameterized(val template: String, val expected: String) {

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{0}={1}")
    fun params() =
        arrayOf(
            arrayOf("{{ nil | strip_html }}", ""),
            arrayOf("{{ 456 | strip_html }}", "456"),
            arrayOf("{{ '45<6' | strip_html }}", "45<6"),
            arrayOf("{{ '<a>' | strip_html }}", ""),
            arrayOf("{{ html | strip_html }}", "123"))
  }

  @Test
  fun run() {
    val json = "{ \"html\" : \"1<h>2</h>3\" }"
    val template = LiquidParser.newBuilder()
        .addFilters(StripHtmlFilter())
        .toParser()
        .parse(template)
    val rendered = template.render(json)
    assert(rendered).isEqualTo(expected)
  }
}

@RunWith(Parameterized::class)
class StripHtmlFilterTest(val name: String, val template: String?, val expected: String?) {
  private val filter = StripHtmlFilter()
  @Test fun run() {
    filter.assertThat()
        .filtering(template)
        .isEqualTo(expected)
  }

  companion object {
    /*
   * def test_strip_html
   *   assert_equal 'test', @filter.strip_html("<div>test</div>")
   *   assert_equal 'test', @filter.strip_html("<div id='test'>test</div>")
   *   assert_equal '', @filter.strip_html("<script type='text/javascript'>document.write('some stuff');</script>")
   *   assert_equal '', @filter.strip_html(nil)
   * end
   */

    @JvmStatic @Parameterized.Parameters(name = "{0}")
    fun params() =
        arrayOf(
            arrayOf("Simple div tags", "<div>test</div>", "test"),
            arrayOf("Simple div tags with attributes", "<div id='test'>test</div>", "test"),
            arrayOf("Script tag", "<script type='text/javascript'>document.write('some stuff');</script>", ""),
            arrayOf("Null content", null, null))
  }
}
