package liqp.tags;

import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.runners.Parameterized;

public class DecrementTest extends LiquifyNoInputTest {
  @Parameterized.Parameters
  public static Object[] testParams() {
    String[][] tests = {
          {"{%decrement port %}", "-1"},
          {"{%decrement port %} {%decrement port%}", "-1 -2"},
          {"{%decrement port %} {%decrement starboard%} {%decrement port %} {%decrement port%} {%decrement starboard " +
                "%}", "-1 -1 -2 -3 -2"},
          {"{% assign x = 42 %}{{x}} {%decrement x %} {%decrement x %} {{x}}", "42 -1 -2 42"},
          {"{% decrement x %} {% decrement x %} {{x}}", "-1 -2 -2"}
    };

    return tests;
  }

  public DecrementTest(@NotNull String templateString,
                       @NotNull String expectedResult) {
    super(templateString, expectedResult, null);
  }
}
