package liqp.nodes;

import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.runners.Parameterized;

public class LtEqNodeTest extends LiquifyNoInputTest {

  @Parameterized.Parameters
  public static Object[] testParams() {

    String[][] tests = {
          {"{% if nil <= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 <= false %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 <= true %}yes{% else %}no{% endif %}", "no"},
          {"{% if a <= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 <= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 <= 42.1000001 %}yes{% else %}no{% endif %}", "yes"},
    };

    return tests;
  }

  public LtEqNodeTest(@NotNull String templateString,
                      @NotNull String expectedResult) {
    super(templateString, expectedResult, null);
  }
}
