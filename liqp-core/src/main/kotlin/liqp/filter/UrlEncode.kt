package liqp.filter

import liqp.context.LContext
import java.net.URLEncoder

class UrlEncode : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return try {
      URLEncoder.encode(context.asString(value), "UTF-8")
    } catch (e: Exception) {
      value
    }
  }
}
