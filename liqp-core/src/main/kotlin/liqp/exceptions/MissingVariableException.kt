package liqp.exceptions

import liqp.context.LLog
import liqp.context.LLogSeverity

class MissingVariableException : RuntimeException {
  val variableName: String

  constructor(variableName: String) : super("Variable '$variableName' does not exist") {
    this.variableName = variableName
  }

  constructor(rootName: String, variableName: String) : super("Variable '$variableName' does not exist (at %$rootName)") {
    this.variableName = rootName
  }
}

class MissingVariable(vararg paths: String) : LLog {
  override val severity = LLogSeverity.WARN
  val variableName: String = paths.joinToString(".")
  override fun toString() = "Variable '$variableName' does not exist"
}
