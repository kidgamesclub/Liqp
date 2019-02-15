package liqp.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.node.LTemplate;
import liqp.parameterized.LiquifyNoInputTest;
import org.antlr.runtime.RecognitionException;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GtNodeTest extends LiquifyNoInputTest {

  @Parameters(name = "getParams")
  public static String[][] getParams() {
    String[][] tests = {
          {"{% if nil > 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 > false %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 > true %}yes{% else %}no{% endif %}", "no"},
          {"{% if a > 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 > 42.09 %}yes{% else %}no{% endif %}", "yes"},
          {"{% if 42.1 > 42.1000001 %}yes{% else %}no{% endif %}", "no"},
    };
    return tests;
  }

  public GtNodeTest(@NotNull String templateString,
                    @NotNull String expectedResult) {
    super(templateString, expectedResult);
  }

  @Test
  public void applyTest() throws RecognitionException {
    LTemplate template = AssertsKt.createTestParser().parse(getTemplateString());
    String rendered = template.render();

    assertThat(rendered, is(getExpectedResult()));
  }
}
