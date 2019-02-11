package liqp.exceptions

class MissingVariableException : RuntimeException {
  val variableName: String

  constructor(variableName: String) : super(String.format("Variable '%s' does not exists", variableName)) {

    this.variableName = variableName
  }

  constructor(rootName: String, variableName: String) : super(String.format("Variable '%s' does not exists (at %s)", rootName, variableName)) {

    this.variableName = rootName
  }
}
