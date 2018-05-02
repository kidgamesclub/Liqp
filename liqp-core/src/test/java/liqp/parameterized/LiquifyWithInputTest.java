package liqp.parameterized;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public abstract class LiquifyWithInputTest {

  public abstract Object[] testParams();

  @Parameters(method = "testParams")
  @Test
  public void run(String templateString, String expectedResult, Object inputData) {
    LTemplate template = LiquidParser.newInstance().parse(templateString);
    String rendered = String.valueOf(template.render(inputData));

    assertThat(rendered).isEqualTo(expectedResult);
  }
}
