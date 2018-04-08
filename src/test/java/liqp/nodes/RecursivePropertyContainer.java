package liqp.nodes;

import liqp.PropertyContainer;

public class RecursivePropertyContainer implements PropertyContainer {
  @Override
  public Object get(String key) {
    if (key.equalsIgnoreCase("title")) {
      return "Lord of the Grapes";
    } else {
      return new RecursivePropertyContainer();
    }
  }
}
