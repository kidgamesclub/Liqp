package liqp.ext.filters.strings
import liqp.nodes.RenderContext


import javax.swing.text.MaskFormatter
import liqp.filters.Filter

/**
 *
 */
class USPhoneNumberFormatFilter :Filter() {

  override fun apply(context: RenderContext, o: Any?, vararg objects: Any): Any? {
    var rtn: String?
    if (o != null) {

      try {
        val phoneMask = "###-###-####"
        var phoneNumber = o.toString().replace("+", "")
        val extraLength = phoneNumber.length - 10
        phoneNumber = phoneNumber.substring(extraLength)

        val maskFormatter = MaskFormatter(phoneMask)
        maskFormatter.valueContainsLiteralCharacters = false
        rtn = maskFormatter.valueToString(phoneNumber)
      } catch (e: Exception) {
        rtn = o.toString()
      }
    } else {
      rtn = null
    }
    return rtn
  }
}
