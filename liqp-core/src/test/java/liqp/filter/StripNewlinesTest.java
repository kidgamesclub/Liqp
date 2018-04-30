package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import liqp.LParser;
import liqp.Template;
import liqp.LiquidParser;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class StripNewlinesTest {

  @Test
  public void applyTest() throws RecognitionException, IOException {

    String json = "{ \"a\" : \"1\\r\\r\\n\\n\\r\\n2\\r3\" }";
    final java.util.Map<String, Object> jsonData = new ObjectMapper().readValue(json, Map.class);

    String[][] tests = {
          {"{{ nil | strip_newlines }}", ""},
          {"{{ a | strip_newlines }}", "123"},
    };

    final LParser ctx = LiquidParser.newBuilder().toParser();

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render(jsonData));

      assertThat(rendered, is(test[1]));
    }
  }

  /*
   * def test_strip_newlines
   *   assert_template_result 'abc', "{{ source | strip_newlines }}", 'source' => "a\nb\nc"
   * end
   */
  @Test
  public void applyOriginalTest() {

    assertThat(LiquidParser.newInstance().parse("{{ source | strip_newlines }}")
          .render(Collections.singletonMap("source", "a\nb\nc")), is((Object) "abc"));
  }
}
