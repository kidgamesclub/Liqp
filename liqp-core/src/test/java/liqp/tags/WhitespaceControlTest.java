package liqp.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

// All output in this test class is tested against Ruby 2.3.1 and Liquid 4.0.0
public class WhitespaceControlTest {

  @Test
  public void noStrip() throws RecognitionException {

    String source = "a  \n  {% assign letter = 'b' %}  \n{{ letter }}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("a..\n....\nb\n..c"));
  }

  @Test
  public void oneLhsStrip() throws RecognitionException {

    String source = "a  \n  {%- assign letter = 'b' %}  \n{{ letter }}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("a..\nb\n..c"));
  }

  @Test
  public void oneRhsStrip() throws RecognitionException {

    String source = "a  \n  {% assign letter = 'b' -%}  \n{{ letter }}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("a..\n..b\n..c"));
  }

  @Test
  public void oneBothStrip() throws RecognitionException {

    String source = "a  \n  {%- assign letter = 'b' -%}  \n{{ letter }}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("ab\n..c"));
  }

  @Test
  public void twoLhsStrip() throws RecognitionException {

    String source = "a  \n  {%- assign letter = 'b' %}  \n{{- letter }}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("ab\n..c"));
  }

  @Test
  public void twoRhsStrip() throws RecognitionException {

    String source = "a  \n  {% assign letter = 'b' -%}  \n{{ letter -}}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("a..\n..bc"));
  }

  @Test
  public void allStrip() throws RecognitionException {

    String source = "a  \n  {%- assign letter = 'b' -%}  \n{{- letter -}}\n  c";
    Template template = LiquidParser.newInstance().parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("abc"));
  }

  @Test
  public void defaultStrip() throws RecognitionException {

    String source = "a  \n  {% assign letter = 'b' %}  \n{{ letter }}\n  c";

    Template template = LiquidParser.newBuilder()
          .stripSpacesAroundTags(true)
          .toParser()
          .parse(source);
    String rendered = String.valueOf(template.render().replace(' ', '.'));

    assertThat(rendered, is("abc"));
  }
}