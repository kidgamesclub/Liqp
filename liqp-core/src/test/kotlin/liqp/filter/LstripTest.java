package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.ZoneId;
import java.util.Locale;
import liqp.AssertsKt;
import liqp.LiquidTemplate;
import liqp.node.LTemplate;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class LstripTest {

  /*
      def test_lstrip
        assert_template_result 'ab c  ', "{{ source | lstrip }}", 'source' => " ab c  "
        assert_template_result "ab c  \n \t", "{{ source | lstrip }}", 'source' => " \tab c  \n \t"
      end
  */
  @Test
  public void applyTest() throws RecognitionException {

    String[][] tests = {
          {"{{ source | lstrip }}", "", "{ \"source\": null }"},
          {"{{ source | lstrip }}", "ab c  ", "{ \"source\": \" ab c  \" }"},
          {"{{ source | lstrip }}", "ab c  \n \t", "{ \"source\": \" \\tab c  \\n \\t\" }"},
    };

    for (String[] test : tests) {

      LTemplate template = AssertsKt.createTestParser().parse(test[0]);
      final String inputData = test[2];
      String rendered = template.renderJson(inputData, Locale.US, ZoneId.systemDefault());

      assertThat(rendered, is(test[1]));
    }
  }
}
