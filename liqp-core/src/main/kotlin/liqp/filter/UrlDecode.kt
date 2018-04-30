package liqp.filter

import liqp.context.LContext
import java.net.URLDecoder

class UrlDecode : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, chain: FilterChainPointer, context: LContext): Any? {
    return try {
      URLDecoder.decode(context.asString(value), "UTF-8")
    } catch (e: Exception) {
      value
    }
  }
}
