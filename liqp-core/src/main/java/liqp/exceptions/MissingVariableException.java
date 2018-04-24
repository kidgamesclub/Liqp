package liqp.exceptions;

public class MissingVariableException extends RuntimeException {
  private final String variableName;

  public MissingVariableException(String variableName) {
    super(String.format("Variable '%s' does not exists", variableName));

    this.variableName = variableName;
  }

  public MissingVariableException(String rootName, String variableName) {
    super(String.format("Variable '%s' does not exists (at %s)", rootName, variableName));

    this.variableName = rootName;
  }

  public String getVariableName() {
    return variableName;
  }
}
