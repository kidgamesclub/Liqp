package liqp.nodes;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class LtEqNodeTest extends LiquifyNoInputTest {

  @Override
  public Object[] testParams() {

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
}
