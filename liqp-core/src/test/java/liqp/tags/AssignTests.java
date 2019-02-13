package liqp.tags;

import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.runners.Parameterized.Parameters;

public class AssignTests extends LiquifyNoInputTest {

  @Parameters
  public static Object[] testParams() {
    return new String[][]{
          {"{% assign name = 'freestyle' %}{{ name }}", "freestyle"},
          {"{% assign age = 42 %}{{ age }}", "42"},
    };
  }

  public AssignTests(@NotNull String templateString,
                     @NotNull String expectedResult) {
    super(templateString, expectedResult, null);
  }
}
