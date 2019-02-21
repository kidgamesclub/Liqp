package liqp.filter

import liqp.context.LContext
import liqp.params.FilterParams
import java.net.URLEncoder

class UrlEncodeFilter : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return try {
      URLEncoder.encode(context.asString(value), "UTF-8")
    } catch (e: Exception) {
      value
    }
  }
}
