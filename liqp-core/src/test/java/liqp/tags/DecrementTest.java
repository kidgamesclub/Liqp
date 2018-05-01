package liqp.tags;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DecrementTest extends LiquifyNoInputTest {
  @Override
  public Object[] testParams() {
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
}
