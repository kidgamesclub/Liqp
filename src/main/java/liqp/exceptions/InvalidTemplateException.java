package liqp.exceptions;

public class InvalidTemplateException extends RuntimeException {

  public InvalidTemplateException(String message) {
    super(message);
  }

  public InvalidTemplateException(String message, Throwable cause) {
    super(message, cause);
  }
}
