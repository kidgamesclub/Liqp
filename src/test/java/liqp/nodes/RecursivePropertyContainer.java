package liqp.nodes;

import liqp.lookup.HasProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecursivePropertyContainer implements HasProperties {

  @Nullable
  @Override
  public Object getProperty(@NotNull String propertyName) {
    if (propertyName.equalsIgnoreCase("title")) {
      return "Lord of the Grapes";
    } else {
      return new RecursivePropertyContainer();
    }
  }
}
