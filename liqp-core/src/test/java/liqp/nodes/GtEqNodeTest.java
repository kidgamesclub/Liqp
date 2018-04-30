package liqp.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import liqp.LiquidParser;
import liqp.Template;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class GtEqNodeTest {

  public static String[][] getParams() {
    String[][] tests = {
          {"{% if nil >= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 >= false %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 >= true %}yes{% else %}no{% endif %}", "no"},
          {"{% if a >= 42.09 %}yes{% else %}no{% endif %}", "no"},
          {"{% if 42.1 >= 42.09 %}yes{% else %}no{% endif %}", "yes"},
          {"{% if 42.1 >= 42.1000001 %}yes{% else %}no{% endif %}", "no"},
    };

    return tests;
  }

  @Test
  @Parameters(method = "getParams")
  public void applyTest(String templateString, String expected) throws RecognitionException {

    Template template = LiquidParser.newInstance().parse(templateString);
    String rendered = String.valueOf(template.render());

    assertThat(rendered, is(expected));
  }
}
