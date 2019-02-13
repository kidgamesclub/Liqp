package liqp.nodes;

import liqp.parameterized.LiquifyNoInputTest;
import org.jetbrains.annotations.NotNull;
import org.junit.runners.Parameterized;

public class GtEqNodeTest extends LiquifyNoInputTest {

  @Parameterized.Parameters
  public static Object[] testParams() {

    return new String[][]{
          {"{% if nil >= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 >= false %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 >= true %}yes{% else %}no{% endif %}", "no"},
          {"{% if a >= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 >= 42.09 %}yes{% else %}no{% endif %}", "yes"},
          {"{% if 42.1 >= 42.1000001 %}yes{% else %}no{% endif %}", "no"},
    };
  }

  public GtEqNodeTest(@NotNull String templateString,
                      @NotNull String expectedResult) {
    super(templateString, expectedResult, null);
  }
}
