package liqp

import liqp.exceptions.ExceededMaxIterationsException
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeoutException

class ProtectionSettingsTest {

  @Test
  fun testWithinMaxRenderTimeMillis() {
    val template = LiquidParser()
        .parse("{% for i in (1..100) %}{{ i }}{% endfor %}")

    template.rendering(
        data = "{\"abc\": \"abcdefghijklmnopqrstuvwxyz\"}",
        renderer = {
          maxRenderTimeMillis = 1000
          executor = Executors.newSingleThreadExecutor()
        })
        .isNotError()
        .contains("234")
  }

  @Test
  fun testExceedMaxRenderTimeMillis() {
    LiquidParser.newInstance()
        .parse("{% for i in (1..10000) %}{{ i }}{% endfor %}")
        .rendering {
          maxRenderTimeMillis = 1
          executor = Executors.newSingleThreadExecutor()
        }
        .hasRenderError(TimeoutException::class.java)
  }

  @Test
  fun testWithinMaxIterationsRange() {
    val template = LiquidParser.newInstance()
        .parse("{% for i in (1..100) %}{{ i }}{% endfor %}")

    template.rendering { maxIterations = 1000 }
        .contains("234")
        .isNotError()
  }

  @Test
  fun testExceedMaxIterationsRange() {
    LiquidParser.newInstance()
        .parse("{% for i in (1..100) %}{{ i }}{% endfor %}")
        .rendering {
          maxIterations = 10
        }
        .hasRenderError(ExceededMaxIterationsException::class.java)
  }

  @Test
  fun testWithinMaxIterationsArray() {

    assertTemplateFactory()
        .withTemplateString("{% for i in array %}{{ i }}{% endfor %}")
        .parsedWithoutError()
        .withEngine { withMaxIterations(1000) }
        .rendering("{\"array\": [1, 2, 3, 4, 5, 6, 7, 8, 9]}")
        .isNotError()
  }

  @Test
  fun testExceedMaxIterationsArray() {

    assertStringTemplate(
        templateString = "{% for i in array %}{{ i }}{% endfor %}",
        data = "{\"array\": [1, 2, 3, 4, 5, 6, 7, 8, 9]}",
        renderer = { withMaxIterations(5) })
        .hasRenderError(ExceededMaxIterationsException::class.java)
  }

  @Test
  fun testExceedMaxIterationsArray2D() {

    assertStringTemplate(
        templateString = "{% for a in array %}{% for i in a %}{{ i }}{% endfor %}{% endfor %}",
        data = "{\"array\": [[1,2,3,4,5], [11,12,13,14,15], [21,22,23,24,25]]}",
        renderer = { withMaxIterations(10) })
        .hasRenderError(RuntimeException::class.java)
  }

  @Test
  fun testWithinMaxIterationsTablerow() {
    assertStringTemplate(
        templateString = "{% tablerow n in collections.frontpage cols:3%} {{n}} {% endtablerow %}",
        data = "{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }")
        .isNotError()
        .isNotNullResult()
  }

  @Test
  fun testExceedMaxIterationsTablerow() {

    assertStringTemplate(
        templateString = "{% tablerow n in collections.frontpage cols:3%} {{n}} {% endtablerow %}",
        data = "{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }", renderer = { withMaxIterations(5) })
        .hasRenderError(RuntimeException::class.java)
  }

  @Test
  fun testWithinMaxTemplateSizeBytes() {
    LiquidParser.newBuilder()
        .maxTemplateSize(3000)
        .toParser()
        .parse("{% tablerow n in collections.frontpage cols:3%} {{n}} {% endtablerow %}")
        .render("{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }")
  }

  @Test(expected = RuntimeException::class)
  fun testExceedMaxTemplateSizeBytes() {
    LiquidParser.newBuilder()
        .maxTemplateSize(30)
        .toParser()
        .parse("{% tablerow n in collections.frontpage cols:3%} {{n}} {% endtablerow %}")
        .render("{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }")
  }

  @Test
  fun testWithinMaxSizeRenderedString() {
    LiquidParser.newBuilder()
        .toParser()
        .parse("{% for i in (1..100) %}{{ abc }}{% endfor %}")
        .rendering(
            data = "{\"abc\": \"abcdefghijklmnopqrstuvwxyz\"}",
            renderer = { maxSizeRenderedString = 2700 })
        .isNotError()
  }

  @Test
  fun testExceedMaxSizeRenderedString() {
    LiquidParser.newBuilder()
        .toParser()
        .parse("{% for i in (1..1000) %}{{ abc }}{% endfor %}")
        .rendering(
            data = "{\"abc\": \"abcdefghijklmnopqrstuvwxyz\"}",
            renderer = {
              maxSizeRenderedString = 2500
            })
        .hasRenderError()
  }
}
