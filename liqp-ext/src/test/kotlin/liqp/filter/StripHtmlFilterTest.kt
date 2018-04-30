package liqp.filter


import liqp.LiquidParser
import liqp.ext.filters.strings.StripHtmlFilter
import liqp.withAssertions
import org.antlr.runtime.RecognitionException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StripHtmlFilterTest {

  @Test
  @Throws(RecognitionException::class)
  fun applyTest() {

    val json = "{ \"html\" : \"1<h>2</h>3\" }"

    val tests = arrayOf(arrayOf("{{ nil | strip_html }}", ""), arrayOf("{{ 456 | strip_html }}", "456"), arrayOf("{{ '45<6' | strip_html }}", "45<6"), arrayOf("{{ '<a>' | strip_html }}", ""), arrayOf("{{ html | strip_html }}", "123"))

    for (test in tests) {

      val template = LiquidParser.newBuilder()
          .addFilters(StripHtmlFilter())
          .toParser()
          .parse(test[0])
      val rendered = template.render(json)

      assertThat(rendered).isEqualTo(test[1])
    }
  }

  /*
   * def test_strip_html
   *   assert_equal 'test', @filter.strip_html("<div>test</div>")
   *   assert_equal 'test', @filter.strip_html("<div id='test'>test</div>")
   *   assert_equal '', @filter.strip_html("<script type='text/javascript'>document.write('some stuff');</script>")
   *   assert_equal '', @filter.strip_html(nil)
   * end
   */
  @Test
  fun applyOriginalTest() {

    val filter = StripHtmlFilter()
    filter.withAssertions()
        .filtering("<div>test</div>")
        .isEqualTo("test")

    filter.withAssertions()
        .filtering("<div id='test'>test</div>")
        .isEqualTo("test")

    filter.withAssertions()
        .filtering("<script type='text/javascript'>document.write('some stuff');\" + \"</script>")
        .isEqualTo("")

    filter.withAssertions()
        .filtering(null)
        .isEqualTo(null)
  }
}
