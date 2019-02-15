package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import assertk.AssertKt;
import liqp.AssertsKt;
import liqp.LParser;
import liqp.LiquidParser;
import liqp.context.LContext;
import liqp.node.LTemplate;
import liqp.params.FilterParams;
import org.antlr.runtime.RecognitionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class FilterTest {

  @Test
  public void testCustomFilter() throws RecognitionException {

    final LFilter custom = new LFilter("textilize") {

      @Nullable
      @Override
      public Object onFilterAction(@NotNull LContext context, @Nullable Object value,
                                   @NotNull FilterParams params) {
        String s = context.asString(value).trim();
        return "<b>" + s.substring(1, s.length() - 1) + "</b>";
      }
    };

    final LParser parser = AssertsKt.createParseSettings()
          .addFilters(custom)
          .build()
          .toParser();
    LTemplate template = parser.parse("{{ '*hi*' | textilize }}");
    String rendered = String.valueOf(template.render());

    assertThat(rendered, is("<b>hi</b>"));
  }
}
