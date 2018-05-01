package liqp.tags;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AssignTests extends LiquifyNoInputTest {

  public Object[] testParams() {
    return new String[][]{
          {"{% assign name = 'freestyle' %}{{ name }}", "freestyle"},
          {"{% assign age = 42 %}{{ age }}", "42"},
    };
  }
}
