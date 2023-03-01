package liqp.filter


import assertk.assertions.isEqualTo
import liqp.assertThat
import liqp.createParseSettings
import liqp.ext.filters.javatime.DurationFilter
import liqp.ext.filters.javatime.PlusDurationFilter
import liqp.toParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DurationFilterTestParameterized(val template: String, val expected: String) {

  companion object {
    @JvmStatic @Parameterized.Parameters(name = "{0}={1}")
    fun params() =
        arrayOf(
            arrayOf("{{ '2012-10-01T00:00:00Z' | plus_duration: 'PT25H' }}", "2012-10-02T01:00Z"),
            arrayOf("{{ 'P3DT12M15S' | duration }}", "3 days, 12 minutes, 15 seconds"))
  }

  @Test
  fun run() {
    val template = createParseSettings()
        .addFilters(PlusDurationFilter())
        .addFilters(DurationFilter())
        .toParser()
        .parse(template)
    val rendered = template.render(emptyMap<String, Any?>())
      assertk.assertThat(rendered).isEqualTo(expected)
  }
}

@RunWith(Parameterized::class)
class DurationFilterTest(val name: String, val template: String?, val expected: String?) {
  private val filter = DurationFilter()
  @Test fun run() {
    filter.assertThat()
        .filtering(template)
        .isEqualTo(expected)
  }

  companion object {

    @JvmStatic @Parameterized.Parameters(name = "{0}")
    fun params() =
        arrayOf(
            arrayOf("Simple duration format", "P3D", "3 days"))
  }
}
