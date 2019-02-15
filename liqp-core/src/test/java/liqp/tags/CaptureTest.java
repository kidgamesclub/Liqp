package liqp.tags;

import static liqp.AssertsKt.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.AssertsKt;
import liqp.LiquidParser;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class CaptureTest {

    @Test
    public void renderTest() throws RecognitionException {

        String[][] tests = {
                {"{% capture foo %}{% endcapture %}{{foo}}", ""},
                {"{% capture foo %}Abc{% endcapture %}{{foo}}", "Abc"}
        };

        for (String[] test : tests) {

            LTemplate template = createTestParser().parse(test[0]);
            String rendered = template.render();

            assertThat(rendered, is(test[1]));
        }
    }

    /*
     * def test_captures_block_content_in_variable
     *   assert_template_result("test string", "{% capture 'var' %}test string{% endcapture %}{{var}}", {})
     * end
     */
    @Test
    public void capturesBlockContentInVariableTest() throws RecognitionException {

        assertThat(createTestParser().parse("{% capture 'var' %}test string{% endcapture %}{{var}}").render(), is("test string"));
    }

    /*
     * def test_capture_to_variable_from_outer_scope_if_existing
     *   template_source = <<-END_TEMPLATE
     *   {% assign var = '' %}
     *   {% if true %}
     *   {% capture var %}first-block-string{% endcapture %}
     *   {% endif %}
     *   {% if true %}
     *   {% capture var %}test-string{% endcapture %}
     *   {% endif %}
     *   {{var}}
     *   END_TEMPLATE
     *   template = TemplateFactory.newInstance().parse(template_source)
     *   rendered = template.render
     *   assert_equal "test-string", rendered.gsub(/\s/, '')
     * end
     */
    @Test
    public void captureToVariableFromOuterScopeIfExistingTest() throws RecognitionException {

        String source = "{% assign var = '' %}\n" +
                "{% if true %}\n" +
                "{% capture var %}first-block-string{% endcapture %}\n" +
                "{% endif %}\n" +
                "{% if true %}\n" +
                "{% capture var %}test-string{% endcapture %}\n" +
                "{% endif %}\n" +
                "{{var}}";

        assertThat(createTestParser().parse(source).render().replaceAll("\\s", ""), is("test-string"));
    }

    /*
     * def test_assigning_from_capture
     *   template_source = <<-END_TEMPLATE
     *   {% assign first = '' %}
     *   {% assign second = '' %}
     *   {% for number in (1..3) %}
     *   {% capture first %}{{number}}{% endcapture %}
     *   {% assign second = first %}
     *   {% endfor %}
     *   {{ first }}-{{ second }}
     *   END_TEMPLATE
     *   template = TemplateFactory.newInstance().parse(template_source)
     *   rendered = template.render
     *   assert_equal "3-3", rendered.gsub(/\s/, '')
     * end
     */
    @Test
    public void assigningFromCaptureTest() throws RecognitionException {

        String source = "{% assign first = '' %}\n" +
                "{% assign second = '' %}\n" +
                "{% for number in (1..3) %}\n" +
                "{% capture first %}{{number}}{% endcapture %}\n" +
                "{% assign second = first %}\n" +
                "{% endfor %}\n" +
                "{{ first }}-{{ second }}";

        assertThat(createTestParser().parse(source).render().replaceAll("\\s", ""), is("3-3"));
    }

    /*
     * def test_capture
     *   assigns = {'var' => 'content' }
     *   assert_template_result('content foo content foo ',
     *                          '{{ var2 }}{% capture var2 %}{{ var }} foo {% endcapture %}{{ var2 }}{{ var2 }}',
     *                          assigns)
     * end
     */
    @Test
    public void captureTest() throws Exception {

        String assigns = "{ \"var\" : \"content\" }";

        assertThat(
                createTestParser().parse("{{ var2 }}{% capture var2 %}{{ var }} foo {% endcapture %}{{ var2 }}{{ var2 }}")
                        .renderJson(assigns),
                is("content foo content foo "));
    }

    /*
     * def test_capture_detects_bad_syntax
     *   assert_raise(SyntaxError) do
     *     assert_template_result('content foo content foo ',
     *                            '{{ var2 }}{% capture %}{{ var }} foo {% endcapture %}{{ var2 }}{{ var2 }}',
     *                            {'var' => 'content' })
     *   end
     * end
     */
    @Test(expected=RuntimeException.class)
    public void capture_detects_bad_syntaxTest() throws Exception {

        createTestParser().parse("{{ var2 }}{% capture %}{{ var }} foo {% endcapture %}{{ var2 }}{{ var2 }}");
    }
}
