package liqp.filter

import liqp.context.LContext
import java.net.URLDecoder

class UrlDecode : LFilter() {

  override fun onFilterAction(context: LContext, value: Any?, params: FilterParams): Any? {
    return try {
      URLDecoder.decode(context.asString(value), "UTF-8")
    } catch (e: Exception) {
      value
    }
  }
}
