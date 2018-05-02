package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.Template;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class IfchangedTest extends LiquifyNoInputTest {
  @Override
  public Object[] testParams() {
        String[][] tests = {
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [1, 1] }", "1"},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [1, 1, 2, 2, 3, 3] }", "123"},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [1, 1, 1, 1] }", "1"},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [] }", ""},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{}", ""}
        };

        return tests;
    }
}
