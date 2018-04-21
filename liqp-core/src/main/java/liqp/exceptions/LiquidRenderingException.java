package liqp.exceptions;

import org.jetbrains.annotations.NotNull;

public class LiquidRenderingException extends RuntimeException {

  public LiquidRenderingException(Throwable cause) {
    super(cause);
  }

  public LiquidRenderingException(@NotNull String message) {
    super(message);
  }
}
