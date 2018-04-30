package liqp.parameterized;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public abstract class LiquifyNoInputTest {

  private final String input;

  protected LiquifyNoInputTest(String input) {
    this.input = input;
  }

  protected LiquifyNoInputTest() {
    this.input = null;
  }

  @Parameters(method = "testParams")
  @Test
  public void run(String templateString, Object expectedResult) {
    LTemplate template = LiquidParser.newInstance().parse(templateString);
    String rendered = String.valueOf(template.render(input));

    assertThat(rendered).isEqualTo(expectedResult);
  }
}
