package liqp.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import liqp.AssertsKt;
import liqp.parameterized.LiquifyNoInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TruncateTest extends LiquifyNoInputTest {
  public TruncateTest() {
    super("{ \"txt\" : \"012345678901234567890123456789012345678901234567890123456789\" }");
  }

  @Override
  public Object[] testParams() {
    String[][] tests = {
          {"{{ nil | truncate }}", ""},
          {"{{ txt | truncate }}", "01234567890123456789012345678901234567890123456..."},
          {"{{ txt | truncate: 5 }}", "01..."},
          {"{{ txt | truncate: 5, '???' }}", "01???"},
          {"{{ txt | truncate: 500, '???' }}", "012345678901234567890123456789012345678901234567890123456789"},
          {"{{ txt | truncate: 2, '===' }}", "==="},
          {"{{ '12345' | truncate: 4, '===' }}", "1==="}
    };
    return tests;
  }

  @Test
  public void truncateNumbers() {
    AssertsKt.assertThat(new Truncate())
          .filtering("1234567890")
          .withParams(7)
          .isEqualTo("1234...");
  }

  @Test
  public void truncateNumbers_Params() {
    AssertsKt.assertThat(new Truncate())
          .filtering("1234567890", 20)
          .isEqualTo("1234567890");
  }

  @Test
  public void truncateNumbers_Empty() {
    AssertsKt.assertThat(new Truncate())
          .filtering("1234567890", 0)
          .isEqualTo("...");
  }

  @Test
  public void truncateNumbers_Full() {
    AssertsKt.assertThat(new Truncate())
          .filtering("1234567890")
          .isEqualTo("1234567890");
  }
}
