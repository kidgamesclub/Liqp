package liqp.filter;

import static liqp.Mocks.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
import java.util.Locale;
import junitparams.JUnitParamsRunner;
import liqp.LiquidDefaults;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DateTest extends LiquifyNoInputTest {

  private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  static final int seconds = 946702800;
  static final java.util.Date date = new java.util.Date(seconds * 1000L);

  private static long seconds(String str) throws Exception {
    long milliSeconds = formatter.parse(str).getTime();
    return milliSeconds / 1000L;
  }

  public static SimpleDateFormat simpleDateFormat(String pattern) {
    return new SimpleDateFormat(pattern, Locale.ENGLISH);
  }

  public Object[] testParams() {
    return new String[][] {
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
          {"{{" + seconds + " | date: '%w'}}", "6"},
          {"{{" + seconds + " | date: '%x'}}", simpleDateFormat("MM/dd/yy").format(date)},
          {"{{" + seconds + " | date: '%X'}}", simpleDateFormat("HH:mm:ss").format(date)},
          {"{{" + seconds + " | date: 'x=%y'}}", "x=" + simpleDateFormat("yy").format(date)},
          {"{{" + seconds + " | date: '%Y'}}", simpleDateFormat("yyyy").format(date)},
          {"{{" + seconds + " | date: '%Z'}}", simpleDateFormat("z").format(date)}
    };
  }

  /*
   * def test_date
   *   assert_equal 'May', @filter.date(Time.parse("2006-05-05 10:00:00"), "%B")
   *   assert_equal 'June', @filter.date(Time.parse("2006-06-05 10:00:00"), "%B")
   *   assert_equal 'July', @filter.date(Time.parse("2006-07-05 10:00:00"), "%B")
   *
   *   assert_equal 'May', @filter.date("2006-05-05 10:00:00", "%B")
   *   assert_equal 'June', @filter.date("2006-06-05 10:00:00", "%B")
   *   assert_equal 'July', @filter.date("2006-07-05 10:00:00", "%B")
   *
   *   assert_equal '2006-07-05 10:00:00', @filter.date("2006-07-05 10:00:00", "")
   *   assert_equal '2006-07-05 10:00:00', @filter.date("2006-07-05 10:00:00", nil)
   *
   *   assert_equal '07/05/2006', @filter.date("2006-07-05 10:00:00", "%m/%d/%Y")
   *
   *   assert_equal "07/16/2004", @filter.date("Fri Jul 16 01:00:00 2004", "%m/%d/%Y")
   *
   *   assert_equal nil, @filter.date(nil, "%B")
   *
   *   assert_equal "07/05/2006", @filter.date(1152098955, "%m/%d/%Y")
   *   assert_equal "07/05/2006", @filter.date("1152098955", "%m/%d/%Y")
   * end
   */
  @Test
  public void applyOriginalTest() throws Exception {

    final LFilter filter = LiquidDefaults.getDefaultFilters().getFilter("date");
    assertThat(filter.onFilterAction(mockRenderContext(), seconds("2006-05-05 10:00:00"), "%B"), is( "May"));
    assertThat(filter.onFilterAction(mockRenderContext(), seconds("2006-06-05 10:00:00"), "%B"), is( "June"));
    assertThat(filter.onFilterAction(mockRenderContext(), seconds("2006-07-05 10:00:00"), "%B"), is( "July"));

    assertThat(filter.onFilterAction(mockRenderContext(), "2006-05-05 10:00:00", "%B"), is("May"));
    assertThat(filter.onFilterAction(mockRenderContext(), "2006-06-05 10:00:00", "%B"), is( "June"));
    assertThat(filter.onFilterAction(mockRenderContext(), "2006-07-05 10:00:00", "%B"), is( "July"));

    assertThat(filter.onFilterAction(mockRenderContext(), "2006-07-05 10:00:00", ""), is( "Wed Jul 05 10:00:00 2006"));
    assertThat(filter.onFilterAction(mockRenderContext(), "2006-07-05 10:00:00", (String) null), is("Wed Jul 05 " +
          "10:00:00 2006"));

    assertThat(filter.onFilterAction(mockRenderContext(), "2006-07-05 10:00:00", "%m/%d/%Y"), is( "07/05/2006"));

    assertThat(filter.onFilterAction(mockRenderContext(), "2006-07-05 10:00", "%m/%d/%Y"), is( "07/05/2006"));
    assertThat(filter.onFilterAction(mockRenderContext(), "2006-07-05", "%m/%d/%Y"), is( "07/05/2006"));

    assertThat(filter.onFilterAction(mockRenderContext(), "Fri Jul 16 01:00:00 2004", "%m/%d/%Y"), is( "07/16/2004"));

    assertThat(filter.onFilterAction(mockRenderContext(), null, "%B"), nullValue());

    assertThat(filter.onFilterAction(mockRenderContext(), 1152098955, "%m/%d/%Y"), is( "07/05/2006"));
    assertThat(filter.onFilterAction(mockRenderContext(), "1152098955", "%m/%d/%Y"), is( "07/05/2006"));
  }
}
