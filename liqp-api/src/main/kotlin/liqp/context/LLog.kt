package liqp.context

interface LLog {
  val severity: LLogSeverity
}

enum class LLogSeverity {
  INFO,
  WARN,
  SEVERE
}