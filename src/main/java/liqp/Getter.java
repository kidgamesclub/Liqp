package liqp;

/**
 * Simple functional interface for retrieving values from known object.
 */
@FunctionalInterface
public interface Getter {
  <I, O> O get(I object);
}
