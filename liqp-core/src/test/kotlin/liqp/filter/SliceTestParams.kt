package liqp.filter

import liqp.assertParser
import liqp.parameterized.LiquifyWithInputTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class SliceTest {
  @Test
  fun noParamsThrowsException() {
    assertParser()
        .withTemplateString("{{ 'mu' | slice }}")
        .rendering()
        .hasRenderError()
  }

  @Test
  fun noIntegerParamThrowsException() {
    assertParser()
        .withTemplateString("{{ 'mu' | slice: false }}")
        .rendering()
        .hasRenderError()
  }

  @Test
  fun noIntegersParamThrowsException() {
    assertParser()
        .withTemplateString("{{ 'mu' | slice: 1, 3.1415 }}")
        .rendering()
        .hasRenderError(ClassCastException::class.java)
  }

  @Test
  fun threeParamsThrowsException() {
    assertParser()
        .withTemplateString("{{ 'mu' | slice: 1, 2, 3 }}")
        .rendering()
        .isEqualTo("u")
  }
}

@RunWith(Parameterized::class)
class SliceTestParams(templateString: String,
                      expectedResult: String?,
                      inputData: Any?) : LiquifyWithInputTest(templateString, expectedResult, inputData) {

  companion object {
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

    @JvmStatic @Parameterized.Parameters(name = "{0}={1}")
    fun testParams() =
        arrayOf(arrayOf("{{ 'foobar' | slice: 1, 3 }}", "oob", "{}"),
            arrayOf("{{ 'foobar' | slice: 1, 1000 }}", "oobar", "{}"),
            arrayOf("{{ 'foobar' | slice: 1, 0 }}", "", "{}"),
            arrayOf("{{ 'foobar' | slice: 1, 1 }}", "o", "{}"),
            arrayOf("{{ 'foobar' | slice: 3, 3 }}", "bar", "{}"),
            arrayOf("{{ 'foobar' | slice: -2, 2 }}", "ar", "{}"),
            arrayOf("{{ 'foobar' | slice: -2, 1000 }}", "ar", "{}"),
            arrayOf("{{ 'foobar' | slice: -1 }}", "r", "{}"),
            arrayOf("{{ nil | slice: 0 }}", "", "{}"),
            arrayOf("{{ nil | slice: 5, 1000 }}", "", "{}"),
            arrayOf("{{ 'foobar' | slice: 100, 10 }}", "", "{}"),
            arrayOf("{{ 'foobar' | slice: 6 }}", "", "{}"),
            arrayOf("{{ 'foobar' | slice: -100, 10 }}", "", "{}"),
            arrayOf("{{ 'foobar' | slice: '1', '3' }}", "oob", "{}"),
            arrayOf("{{ x | slice: 1 }}", "2", "{ \"x\": [1, 2, 3, 4, 5] }"),
            arrayOf("{{ x | slice: 1, 3 }}", "234", "{ \"x\": [1, 2, 3, 4, 5] }"),
            arrayOf("{{ x | slice: 1, 3000 }}", "2345", "{ \"x\": [1, 2, 3, 4, 5] }"),
            arrayOf("{{ x | slice: -2, 2 }}", "45", "{ \"x\": [1, 2, 3, 4, 5] }"))
  }
}
