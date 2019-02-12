package liqp.filter

import assertk.assert
import assertk.assertions.isEqualTo
import liqp.LiquidDefaults
import liqp.LiquidParser
import liqp.Mocks
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.text.SimpleDateFormat
import java.util.Locale.ENGLISH

@RunWith(Parameterized::class)
class DateTestFilterParameterized(val input: Any?, val args: List<Any?>, val expected: String?) {
 @Test fun run() {
   assertk.assert(filter.onFilterAction(context, input, *args.toTypedArray())).isEqualTo(expected)
 }
  companion object {
    @JvmStatic @Parameters(name = "{0}") fun params() = arrayOf(
        arrayOf(secondsOf("2006-05-05 10:00:00"), listOf("%B"), "May"),
        arrayOf(secondsOf("2006-06-05 10:00:00"), listOf("%B"), "June"),
        arrayOf(secondsOf("2006-07-05 10:00:00"), listOf("%B"), "July"),
        arrayOf("2006-05-05 10:00:00", listOf("%B"), "May"),
        arrayOf("2006-06-05 10:00:00", listOf("%B"), "June"),
        arrayOf("2006-07-05 10:00:00", listOf("%B"), "July"),
        arrayOf("2006-07-05 10:00:00", listOf(""), "Wed Jul 05 10:00:00 2006"),
        arrayOf("2006-07-05 10:00:00", listOf(null), "Wed Jul 05 10:00:00 2006"),
        arrayOf("2006-07-05 10:00:00", listOf("%m/%d/%Y"), "07/05/2006"),
        arrayOf("2006-07-05 10:00", listOf("%m/%d/%Y"), "07/05/2006"),
        arrayOf("2006-07-05", listOf("%m/%d/%Y"), "07/05/2006"),
        arrayOf("Fri Jul 16 01:00:00 2004", listOf("%m/%d/%Y"), "07/16/2004"),
        arrayOf(null, listOf("%B"), null),
        arrayOf(1152098955, listOf("%m/%d/%Y"), "07/05/2006"),
        arrayOf("1152098955", listOf("%m/%d/%Y"), "07/05/2006"))
  }
}

@RunWith(Parameterized::class)
class DateTestParameterized(val template: String, val expected: String) {

  @Test
  fun run() {
    val template = LiquidParser.newInstance().parse(template)
    val rendered = template.render()
    assert(rendered).isEqualTo(expected)
  }

  companion object {
    @JvmStatic @Parameters(name = "{0}={1}") fun testParams() = arrayOf(
        arrayOf("{{$seconds | date: 'mu'}}", "mu"),
        arrayOf("{{$seconds | date: '%'}}", "%"),
        arrayOf("{{$seconds | date: '%? %%'}}", "%? %"),
        arrayOf("{{$seconds | date: '%a'}}", simpleDateFormat("EEE").format(date)),
        arrayOf("{{$seconds | date: '%A'}}", simpleDateFormat("EEEE").format(date)),
        arrayOf("{{$seconds | date: '%b'}}", simpleDateFormat("MMM").format(date)),
        arrayOf("{{$seconds | date: '%B'}}", simpleDateFormat("MMMM").format(date)),
        arrayOf("{{$seconds | date: '%c'}}", simpleDateFormat("EEE MMM dd HH:mm:ss yyyy").format(date)),
        arrayOf("{{$seconds | date: '%d'}}", simpleDateFormat("dd").format(date)),
        arrayOf("{{$seconds | date: '%H'}}", simpleDateFormat("HH").format(date)),
        arrayOf("{{$seconds | date: '%I'}}", simpleDateFormat("hh").format(date)),
        arrayOf("{{$seconds | date: '%j'}}", simpleDateFormat("DDD").format(date)),
        arrayOf("{{$seconds | date: '%m'}}", simpleDateFormat("MM").format(date)),
        arrayOf("{{$seconds | date: '%M'}}", simpleDateFormat("mm").format(date)),
        arrayOf("{{$seconds | date: '%p'}}", simpleDateFormat("a").format(date)),
        arrayOf("{{$seconds | date: '%S'}}", simpleDateFormat("ss").format(date)),
        arrayOf("{{$seconds | date: '%U'}}", simpleDateFormat("ww").format(date)),
        arrayOf("{{$seconds | date: '%W'}}", simpleDateFormat("ww").format(date)),
        arrayOf("{{$seconds | date: '%w'}}", "6"),
        arrayOf("{{$seconds | date: '%x'}}", simpleDateFormat("MM/dd/yy").format(date)),
        arrayOf("{{$seconds | date: '%X'}}", simpleDateFormat("HH:mm:ss").format(date)),
        arrayOf("{{$seconds | date: 'x=%y'}}", "x=" + simpleDateFormat("yy").format(date)),
        arrayOf("{{$seconds | date: '%Y'}}", simpleDateFormat("yyyy").format(date)),
        arrayOf("{{$seconds | date: '%Z'}}", simpleDateFormat("z").format(date)))

    fun simpleDateFormat(pattern: String): SimpleDateFormat {
      return SimpleDateFormat(pattern, ENGLISH)
    }
  }
}

private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", ENGLISH)
internal val seconds = 946702800
internal val date = java.util.Date(seconds * 1000L)
val filter = LiquidDefaults.defaultFilters["date"]
private fun secondsOf(str: String): Long {
  return formatter.parse(str).time / 1000L
}
