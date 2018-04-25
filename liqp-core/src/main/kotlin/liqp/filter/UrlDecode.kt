package liqp.filter

import java.net.URLDecoder
import liqp.context.LContext

class UrlDecode : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {

    try {
      return URLDecoder.decode(super.asString(value), "UTF-8")
    } catch (e: Exception) {
      return value
    }
  }
}
