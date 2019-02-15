package liqp.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidTemplate;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class EqNodeTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{% if 1.0 == 1 %}TRUE{% else %}FALSE{% endif %}", "TRUE"},
                {"{% if nil == nil %}TRUE{% else %}FALSE{% endif %}", "TRUE"},
                {"{% if false == false %}TRUE{% else %}FALSE{% endif %}", "TRUE"},
                {"{% if \"\" == '' %}TRUE{% else %}FALSE{% endif %}", "TRUE"},
        };

        for (String[] test : tests) {

            LTemplate template = AssertsKt.createTestParser().parse(test[0]);
            String rendered = template.render();

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_illegal_symbols
     *   assert_template_result('', '{% if true == empty %}?{% endif %}', {})
     *   assert_template_result('', '{% if true == null %}?{% endif %}', {})
     *   assert_template_result('', '{% if empty == true %}?{% endif %}', {})
     *   assert_template_result('', '{% if null == true %}?{% endif %}', {})
     * end
     */
    @Test
    public void illegal_symbolsTest() throws Exception {

        assertThat(AssertsKt.createTestParser().parse("{% if true == empty %}?{% endif %}").render(), is(""));
        assertThat(AssertsKt.createTestParser().parse("{% if true == null %}?{% endif %}").render(), is(""));
        assertThat(AssertsKt.createTestParser().parse("{% if empty == true %}?{% endif %}").render(), is(""));
        assertThat(AssertsKt.createTestParser().parse("{% if null == true %}?{% endif %}").render(), is(""));
    }
}
