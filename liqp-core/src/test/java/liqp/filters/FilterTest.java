package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import liqp.Template;
import liqp.TemplateFactory;
import liqp.nodes.RenderContext;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class FilterTest {

    @Test
    public void testCustomFilter() throws RecognitionException {

      final Filter custom = new Filter("textilize") {
        @Override
        public Object apply(RenderContext context, Object value, Object... params) {
          String s = super.asString(value).trim();
          return "<b>" + s.substring(1, s.length() - 1) + "</b>";
        }
      };

      Template template = TemplateFactory.newBuilder().withFilters(custom).parse("{{ '*hi*' | textilize }}");
        String rendered = String.valueOf(template.render());

        assertThat(rendered, is("<b>hi</b>"));
    }
}
