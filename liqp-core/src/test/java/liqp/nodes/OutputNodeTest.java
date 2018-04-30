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
public class OutputNodeTest {

  public static String[] keywordParams() {
    return new String[]{
          "capture",
          "endcapture",
          "comment",
          "endcomment",
          "raw",
          "endraw",
          "if",
          "elsif",
          "endif",
          "unless",
          "endunless",
          "else",
          "contains",
          "case",
          "endcase",
          "when",
          "cycle",
          "for",
          "endfor",
          "in",
          "and",
          "or",
          "tablerow",
          "endtablerow",
          "assign",
          "include",
          "with",
          "end",
          "break",
          "continue",
    };
  }

  public static String[][] badKeywordVariables() {
    return new String[][]{
          {"true", "true"},
          {"false", "false"},
          {"nil", ""},
          {"null", ""},
          // {"empty", "Object"},
    };
  }

  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ X }}", "mu"},
          {"{{ 'a.b.c' | split:'.' | first | upcase }}", "A"},
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render("{\"X\" : \"mu\"}"));

      assertThat(rendered, is(test[1]));
    }
  }

  @Test
  @Parameters(method = "keywordParams")
  public void testKeywordsAsVariables(String keyword) {

    String test = "{{" + keyword + "}}";
    String expected = keyword + "_" + Integer.toString(keyword.length());
    String json = "{\"" + keyword + "\" : \"" + expected + "\" }";
    Template template = LiquidParser.newInstance().parse(test);
    String rendered = template.render(json);

    assertThat(rendered, is(expected));
  }

  @Test
  @Parameters(method = "badKeywordVariables")
  public void badKeywordAsVariableTest(String keyword, String expected) {
    String test = "{{" + keyword + "}}";
    String json = "{\"" + keyword + "\" : \"bad\" }";
    Template template = LiquidParser.newInstance().parse(test);
    String rendered = template.render(json);

    assertThat(rendered, is(expected));
  }
}
