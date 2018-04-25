package liqp.filter

import java.net.URLEncoder
import liqp.context.LContext

class UrlEncode : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    try {
      return URLEncoder.encode(super.asString(value), "UTF-8")
    } catch (e: Exception) {
      return value
    }
  }
}
