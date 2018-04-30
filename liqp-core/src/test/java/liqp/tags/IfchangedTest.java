package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.junit.Test;

public class IfchangedTest {

    //  def test_ifchanged
    //
    //    assigns = { 'array' => [ 1, 1, 2, 2, 3, 3] }
    //    assert_template_result('123', '{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}', assigns)
    //
    //    assigns = { 'array' => [ 1, 1, 1, 1] }
    //    assert_template_result('1', '{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}', assigns)
    //  end
    @Test
    public void renderTest() {

        String[][] tests = {
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [1, 1] }", "1"},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [1, 1, 2, 2, 3, 3] }", "123"},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [1, 1, 1, 1] }", "1"},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{ \"array\": [] }", ""},
                {"{%for item in array%}{%ifchanged%}{{item}}{% endifchanged %}{%endfor%}", "{}", ""}
        };

        for (String[] test : tests) {

            LTemplate template = LiquidParser.newInstance().parse(test[0]);
            String rendered = String.valueOf(template.render(test[1]));

            assertThat(rendered, is(test[2]));
        }
    }
}
