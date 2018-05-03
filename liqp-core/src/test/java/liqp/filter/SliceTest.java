package liqp.filter;

import junitparams.JUnitParamsRunner;
import liqp.AssertsKt;
import liqp.LiquidParser;
import liqp.parameterized.LiquifyWithInputTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class SliceTest extends LiquifyWithInputTest {

    /*
        def test_slice
          assert_equal 'oob', @filter.slice('foobar', 1, 3)
          assert_equal 'oobar', @filter.slice('foobar', 1, 1000)
          assert_equal '', @filter.slice('foobar', 1, 0)
          assert_equal 'o', @filter.slice('foobar', 1, 1)
          assert_equal 'bar', @filter.slice('foobar', 3, 3)
          assert_equal 'ar', @filter.slice('foobar', -2, 2)
          assert_equal 'ar', @filter.slice('foobar', -2, 1000)
          assert_equal 'r', @filter.slice('foobar', -1)
          assert_equal '', @filter.slice(nil, 0)
          assert_equal '', @filter.slice('foobar', 100, 10)
          assert_equal '', @filter.slice('foobar', -100, 10)
          assert_equal 'oob', @filter.slice('foobar', '1', '3')
        end
    */

  public Object[] testParams() {

    String[][] tests = {
          {"{{ 'foobar' | slice: 1, 3 }}", "oob", "{}"},
          {"{{ 'foobar' | slice: 1, 1000 }}", "oobar", "{}"},
          {"{{ 'foobar' | slice: 1, 0 }}", "", "{}"},
          {"{{ 'foobar' | slice: 1, 1 }}", "o", "{}"},
          {"{{ 'foobar' | slice: 3, 3 }}", "bar", "{}"},
          {"{{ 'foobar' | slice: -2, 2 }}", "ar", "{}"},
          {"{{ 'foobar' | slice: -2, 1000 }}", "ar", "{}"},
          {"{{ 'foobar' | slice: -1 }}", "r", "{}"},
          {"{{ nil | slice: 0 }}", "", "{}"},
          {"{{ nil | slice: 5, 1000 }}", "", "{}"},
          {"{{ 'foobar' | slice: 100, 10 }}", "", "{}"},
          {"{{ 'foobar' | slice: 6 }}", "", "{}"},
          {"{{ 'foobar' | slice: -100, 10 }}", "", "{}"},
          {"{{ 'foobar' | slice: '1', '3' }}", "oob", "{}"},
          {"{{ x | slice: 1 }}", "2", "{ \"x\": [1, 2, 3, 4, 5] }"},
          {"{{ x | slice: 1, 3 }}", "234", "{ \"x\": [1, 2, 3, 4, 5] }"},
          {"{{ x | slice: 1, 3000 }}", "2345", "{ \"x\": [1, 2, 3, 4, 5] }"},
          {"{{ x | slice: -2, 2 }}", "45", "{ \"x\": [1, 2, 3, 4, 5] }"},
    };

    return tests;
  }

  @Test
  public void noParamsThrowsException() {
    AssertsKt.assertParser()
          .withTemplateString("{{ 'mu' | slice }}")
          .rendering()
          .hasRenderError();
  }

  @Test
  public void noIntegerParamThrowsException() {
    AssertsKt.assertParser()
          .withTemplateString("{{ 'mu' | slice: false }}")
          .rendering()
          .hasRenderError();
  }

  @Test
  public void noIntegersParamThrowsException() {
    AssertsKt.assertParser()
          .withTemplateString("{{ 'mu' | slice: 1, 3.1415 }}")
          .rendering()
          .isEqualTo("u");
  }

  @Test
  public void threeParamsThrowsException() {
    AssertsKt.assertParser()
          .withTemplateString("{{ 'mu' | slice: 1, 2, 3 }}")
          .rendering()
          .isEqualTo("u");
  }
}
