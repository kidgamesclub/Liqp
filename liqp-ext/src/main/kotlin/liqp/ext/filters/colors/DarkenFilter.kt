package liqp.ext.filters.colors

import liqp.context.LContext
import liqp.filter.FilterParams
import liqp.filter.LFilter
import java.awt.Color

class DarkenFilter : LFilter() {

override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    var ret = value

    if (value is String && params.size == 1) {
      val darkenAmount = params[0, 0.0]

      val decoded = Color.decode(value)
      val darker = darker(decoded, darkenAmount)

      ret = "#" + Integer.toHexString(darker.rgb).substring(2).toUpperCase()
    }

    return ret
  }

  private fun darker(color: Color, fraction: Double): Color {
    var red = Math.round(color.red * (1.0 - fraction)).toInt()
    var green = Math.round(color.green * (1.0 - fraction)).toInt()
    var blue = Math.round(color.blue * (1.0 - fraction)).toInt()

    if (red < 0) red = 0 else if (red > 255) red = 255
    if (green < 0) green = 0 else if (green > 255) green = 255
    if (blue < 0) blue = 0 else if (blue > 255) blue = 255

    val alpha = color.alpha

    return Color(red, green, blue, alpha)
  }
}
