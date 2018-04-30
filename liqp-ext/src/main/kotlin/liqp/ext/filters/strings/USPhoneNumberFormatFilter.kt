package liqp.ext.filters.strings

import liqp.context.LContext
import liqp.filter.FilterChainPointer
import liqp.filter.FilterParams
import liqp.filter.LFilter
import javax.swing.text.MaskFormatter

/**
 *
 */
class USPhoneNumberFormatFilter : LFilter() {

  override fun onFilterAction(params: FilterParams, value: Any?, context: LContext): Any? {

    var rtn: String?
    if (value != null) {

      try {
        val phoneMask = "###-###-####"
        var phoneNumber = value.toString().replace("+", "")
        val extraLength = phoneNumber.length - 10
        phoneNumber = phoneNumber.substring(extraLength)

        val maskFormatter = MaskFormatter(phoneMask)
        maskFormatter.valueContainsLiteralCharacters = false
        rtn = maskFormatter.valueToString(phoneNumber)
      } catch (e: Exception) {
        rtn = value.toString()
      }
    } else {
      rtn = null
    }
    return rtn
  }
}
