package liqp

import liqp.context.LLog
import liqp.context.LLogSeverity

data class LiquidLog(val message: Any, override val severity: LLogSeverity) : LLog {
  override fun toString() = "$message"
  companion object {
    fun severe(message:Any) = LiquidLog(message, severity = LLogSeverity.SEVERE)
    fun warn(message:Any) = LiquidLog(message, severity = LLogSeverity.WARN)
    fun info(message:Any) = LiquidLog(message, severity = LLogSeverity.INFO)
  }
}


