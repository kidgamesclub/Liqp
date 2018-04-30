package liqp.filter

import liqp.context.LContext
import java.net.URLEncoder

class UrlEncode : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return try {
      URLEncoder.encode(context.asString(value), "UTF-8")
    } catch (e: Exception) {
      value
    }
  }
}
