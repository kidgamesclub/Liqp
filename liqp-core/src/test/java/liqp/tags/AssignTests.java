package liqp.tags;

import liqp.parameterized.LiquifyNoInputTest;

public class AssignTests extends LiquifyNoInputTest {

  public Object[] testParams() {
    return new String[][]{
          {"{% assign name = 'freestyle' %}{{ name }}", "freestyle"},
          {"{% assign age = 42 %}{{ age }}", "42"},
    };
  }
}
