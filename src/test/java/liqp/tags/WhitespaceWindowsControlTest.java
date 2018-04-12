package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class WhitespaceWindowsControlTest {

    @Test
    public void noStrip() throws RecognitionException {

        String source = "a  \r\n  {% assign letter = 'b' %}  \r\n{{ letter }}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("a..\r\n....\r\nb\r\n..c"));
    }

    @Test
    public void oneLhsStrip() throws RecognitionException {

        String source = "a  \r\n  {%- assign letter = 'b' %}  \r\n{{ letter }}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("a..\r\nb\r\n..c"));
    }

    @Test
    public void oneRhsStrip() throws RecognitionException {

        String source = "a  \r\n  {% assign letter = 'b' -%}  \r\n{{ letter }}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("a..\r\n..b\r\n..c"));
    }

    @Test
    public void oneBothStrip() throws RecognitionException {

        String source = "a  \r\n  {%- assign letter = 'b' -%}  \r\n{{ letter }}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("ab\r\n..c"));
    }

    @Test
    public void twoLhsStrip() throws RecognitionException {

        String source = "a  \r\n  {%- assign letter = 'b' %}  \r\n{{- letter }}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("ab\r\n..c"));
    }

    @Test
    public void twoRhsStrip() throws RecognitionException {

        String source = "a  \r\n  {% assign letter = 'b' -%}  \r\n{{ letter -}}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("a..\r\n..bc"));
    }

    @Test
    public void allStrip() throws RecognitionException {

        String source = "a  \r\n  {%- assign letter = 'b' -%}  \r\n{{- letter -}}\r\n  c";
        Template template = TemplateFactory.newBuilder().parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("abc"));
    }

    @Test
    public void defaultStrip() throws RecognitionException {

        String source = "a  \r\n  {% assign letter = 'b' %}  \r\n{{ letter }}\r\n  c";
        Template template = TemplateFactory.newBuilder().stripSpacesAroundTags(true).parse(source);
        String rendered = String.valueOf(template.render().replace(' ', '.'));

        assertThat(rendered, is("abc"));
    }
}