package liqp.tags;

import junitparams.JUnitParamsRunner;
import liqp.parameterized.LiquifyWithInputTest;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class IfchangedTest extends LiquifyWithInputTest {
  @Override
  public Object[] testParams() {
    return new String[][]{
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "1",
                "{ \"array\": [1, 1] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "123",
                "{ \"array\": [1, 1, 2, 2, 3, 3] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "1",
                "{ \"array\": [1, 1, 1, 1] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "",
                "{ \"array\": [] }"},
          {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}",
                "",
                "{}"}
    };
  }
}
