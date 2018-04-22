package liqp.filters;

import static java.util.Collections.emptyMap;
import static liqp.Mocks.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
import java.util.Locale;
import liqp.Template;
import liqp.LiquidRenderer;
import liqp.LiquidParser;
import liqp.nodes.RenderContext;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

public class DateTest {

  private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

  private static long seconds(String str) throws Exception {
    long milliSeconds = formatter.parse(str).getTime();
    return milliSeconds / 1000L;
  }

  public static SimpleDateFormat simpleDateFormat(String pattern) {
    return new SimpleDateFormat(pattern, Locale.ENGLISH);
  }

  @Test
  public void applyTest() throws RecognitionException {

    final int seconds = 946702800;
    final java.util.Date date = new java.util.Date(seconds * 1000L);

    String[][] tests = {
          {"{{" + seconds + " | date: 'mu'}}", "mu"},
          {"{{" + seconds + " | date: '%'}}", "%"},
          {"{{" + seconds + " | date: '%? %%'}}", "%? %"},
          {"{{" + seconds + " | date: '%a'}}", simpleDateFormat("EEE").format(date)},
          {"{{" + seconds + " | date: '%A'}}", simpleDateFormat("EEEE").format(date)},
          {"{{" + seconds + " | date: '%b'}}", simpleDateFormat("MMM").format(date)},
          {"{{" + seconds + " | date: '%B'}}", simpleDateFormat("MMMM").format(date)},
          {"{{" + seconds + " | date: '%c'}}", simpleDateFormat("EEE MMM dd HH:mm:ss yyyy").format(date)},
          {"{{" + seconds + " | date: '%d'}}", simpleDateFormat("dd").format(date)},
          {"{{" + seconds + " | date: '%H'}}", simpleDateFormat("HH").format(date)},
          {"{{" + seconds + " | date: '%I'}}", simpleDateFormat("hh").format(date)},
          {"{{" + seconds + " | date: '%j'}}", simpleDateFormat("DDD").format(date)},
          {"{{" + seconds + " | date: '%m'}}", simpleDateFormat("MM").format(date)},
          {"{{" + seconds + " | date: '%M'}}", simpleDateFormat("mm").format(date)},
          {"{{" + seconds + " | date: '%p'}}", simpleDateFormat("a").format(date)},
          {"{{" + seconds + " | date: '%S'}}", simpleDateFormat("ss").format(date)},
          {"{{" + seconds + " | date: '%U'}}", simpleDateFormat("ww").format(date)},
          {"{{" + seconds + " | date: '%W'}}", simpleDateFormat("ww").format(date)},
          {"{{" + seconds + " | date: '%w'}}", simpleDateFormat("F").format(date)},
          {"{{" + seconds + " | date: '%x'}}", simpleDateFormat("MM/dd/yy").format(date)},
          {"{{" + seconds + " | date: '%X'}}", simpleDateFormat("HH:mm:ss").format(date)},
          {"{{" + seconds + " | date: 'x=%y'}}", "x=" + simpleDateFormat("yy").format(date)},
          {"{{" + seconds + " | date: '%Y'}}", simpleDateFormat("yyyy").format(date)},
          {"{{" + seconds + " | date: '%Z'}}", simpleDateFormat("z").format(date)}
    };

    for (String[] test : tests) {

      Template template = LiquidParser.newInstance().parse(test[0]);
      String rendered = String.valueOf(template.render());

      assertThat(rendered, is(test[1]));
    }
  }

  /*
   * def test_date
   *   assert_equal 'May', @filters.date(Time.parse("2006-05-05 10:00:00"), "%B")
   *   assert_equal 'June', @filters.date(Time.parse("2006-06-05 10:00:00"), "%B")
   *   assert_equal 'July', @filters.date(Time.parse("2006-07-05 10:00:00"), "%B")
   *
   *   assert_equal 'May', @filters.date("2006-05-05 10:00:00", "%B")
   *   assert_equal 'June', @filters.date("2006-06-05 10:00:00", "%B")
   *   assert_equal 'July', @filters.date("2006-07-05 10:00:00", "%B")
   *
   *   assert_equal '2006-07-05 10:00:00', @filters.date("2006-07-05 10:00:00", "")
   *   assert_equal '2006-07-05 10:00:00', @filters.date("2006-07-05 10:00:00", nil)
   *
   *   assert_equal '07/05/2006', @filters.date("2006-07-05 10:00:00", "%m/%d/%Y")
   *
   *   assert_equal "07/16/2004", @filters.date("Fri Jul 16 01:00:00 2004", "%m/%d/%Y")
   *
   *   assert_equal nil, @filters.date(nil, "%B")
   *
   *   assert_equal "07/05/2006", @filters.date(1152098955, "%m/%d/%Y")
   *   assert_equal "07/05/2006", @filters.date("1152098955", "%m/%d/%Y")
   * end
   */
  @Test
  public void applyOriginalTest() throws Exception {

    final Filter filter = Filter.getFilter("date");
    final RenderContext context = new RenderContext(emptyMap(),
          new LiquidParser(),
          new LiquidRenderer());
    assertThat(filter.apply(mockRenderContext(), seconds("2006-05-05 10:00:00"), "%B"), is((Object) "May"));
    assertThat(filter.apply(mockRenderContext(), seconds("2006-06-05 10:00:00"), "%B"), is((Object) "June"));
    assertThat(filter.apply(mockRenderContext(), seconds("2006-07-05 10:00:00"), "%B"), is((Object) "July"));

    assertThat(filter.apply(mockRenderContext(), "2006-05-05 10:00:00", "%B"), is((Object) "May"));
    assertThat(filter.apply(mockRenderContext(), "2006-06-05 10:00:00", "%B"), is((Object) "June"));
    assertThat(filter.apply(mockRenderContext(), "2006-07-05 10:00:00", "%B"), is((Object) "July"));

    assertThat(filter.apply(mockRenderContext(), "2006-07-05 10:00:00", ""), is((Object) "2006-07-05 10:00:00"));
    assertThat(filter.apply(mockRenderContext(), "2006-07-05 10:00:00", (RenderContext) null), is((Object) "2006-07-05 10:00:00"));

    assertThat(filter.apply(mockRenderContext(), "2006-07-05 10:00:00", "%m/%d/%Y"), is((Object) "07/05/2006"));

    assertThat(filter.apply(mockRenderContext(), "Fri Jul 16 01:00:00 2004", "%m/%d/%Y"), is((Object) "07/16/2004"));

    assertThat(filter.apply(mockRenderContext(), null, "%B"), is((Object) null));

    assertThat(filter.apply(mockRenderContext(), 1152098955, "%m/%d/%Y"), is((Object) "07/05/2006"));
    assertThat(filter.apply(mockRenderContext(), "1152098955", "%m/%d/%Y"), is((Object) "07/05/2006"));
  }
}
