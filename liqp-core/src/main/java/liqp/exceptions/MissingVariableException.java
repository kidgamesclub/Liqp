package liqp.exceptions;


public class MissingVariableException extends RuntimeException {
    private final String variableName;

    public MissingVariableException(String variableName) {
        super(String.format("Variable '%s' does not exists", variableName));

        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
