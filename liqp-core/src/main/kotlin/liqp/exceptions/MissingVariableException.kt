package liqp.exceptions

class MissingVariableException : RuntimeException {
  val variableName: String

  constructor(variableName: String) : super("Variable '$variableName' does not exist") {
    this.variableName = variableName
  }

  constructor(rootName: String, variableName: String) : super("Variable '$variableName' does not exist (at %$rootName)") {
    this.variableName = rootName
  }
}
