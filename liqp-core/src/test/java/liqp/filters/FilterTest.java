package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.LValue;
import liqp.Template;
import liqp.LiquidParser;
import liqp.nodes.RenderContext;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class FilterTest {

    @Test
    public void testCustomFilter() throws RecognitionException {

      final Filter custom = new Filter("textilize") {
        @Override
        public Object apply(RenderContext context, Object value, Object... params) {
          String s = asString(value).trim();
          return "<b>" + s.substring(1, s.length() - 1) + "</b>";
        }
      };

      final LiquidParser parser = LiquidParser.newBuilder()
            .addFilters(custom)
            .toParser();
      Template template = parser.parse("{{ '*hi*' | textilize }}");
        String rendered = String.valueOf(template.render());

        assertThat(rendered, is("<b>hi</b>"));
    }
}
