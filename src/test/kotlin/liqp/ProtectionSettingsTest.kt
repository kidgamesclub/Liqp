package liqp

import liqp.exceptions.ExceededMaxIterationsException
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeoutException

class ProtectionSettingsTest {

  @Test
  fun testWithinMaxRenderTimeMillis() {
    val template = TemplateFactory()
        .parse("{% for i in (1..100) %}{{ i }}{% endfor %}")

    template.assertRenderResult(
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
    val template = TemplateFactory.newInstance()
        .parse("{% for i in (1..10000) %}{{ i }}{% endfor %}")
        .assertRenderResult {
          maxRenderTimeMillis = 1
          executor = Executors.newSingleThreadExecutor()
        }
        .isRenderError(TimeoutException::class.java)
  }

  @Test
  fun testWithinMaxIterationsRange() {
    val template = TemplateFactory.newBuilder()
        .parse("{% for i in (1..100) %}{{ i }}{% endfor %}")

    template.assertRenderResult { maxIterations = 1000 }
        .contains("234")
        .isNotError()
  }

  @Test
  fun testExceedMaxIterationsRange() {
    TemplateFactory.newInstance()
        .parse("{% for i in (1..100) %}{{ i }}{% endfor %}")
        .assertRenderResult{
          maxIterations = 10
        }
        .isRenderError(ExceededMaxIterationsException::class.java)
  }

  @Test
  fun testWithinMaxIterationsArray() {

    assertTemplateFactory()
        .withTemplateString("{% for i in array %}{{ i }}{% endfor %}")
        .parsedWithoutError()
        .withEngine { maxIterations(1000) }
        .rendering("{\"array\": [1, 2, 3, 4, 5, 6, 7, 8, 9]}")
        .isNotError()
  }

  @Test
  fun testExceedMaxIterationsArray() {

    assertStringTemplate(
        templateString = "{% for i in array %}{{ i }}{% endfor %}",
        data = "{\"array\": [1, 2, 3, 4, 5, 6, 7, 8, 9]}",
        renderer = { maxIterations(5) })
        .isRenderError(ExceededMaxIterationsException::class.java)
  }

  @Test
  fun testExceedMaxIterationsArray2D() {

    assertStringTemplate(
        templateString = "{% for a in array %}{% for i in a %}{{ i }}{% endfor %}{% endfor %}",
        data = "{\"array\": [[1,2,3,4,5], [11,12,13,14,15], [21,22,23,24,25]]}",
        renderer = { maxIterations(10) })
        .isRenderError(RuntimeException::class.java)
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
        data = "{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }", renderer = { maxIterations(5) })
        .isRenderError(RuntimeException::class.java)
  }

  @Test
  fun testWithinMaxTemplateSizeBytes() {
    TemplateFactory.newBuilder()
        .maxTemplateSize(3000)
        .parse("{% tablerow n in collections.frontpage cols:3%} {{n}} {% endtablerow %}")
        .render("{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }")
  }

  @Test(expected = RuntimeException::class)
  fun testExceedMaxTemplateSizeBytes() {
    TemplateFactory.newBuilder()
        .maxTemplateSize(30)
        .parse("{% tablerow n in collections.frontpage cols:3%} {{n}} {% endtablerow %}")
        .render("{ \"collections\" : { \"frontpage\" : [1,2,3,4,5,6] } }")
  }

  @Test
  fun testWithinMaxSizeRenderedString() {
    TemplateFactory.newBuilder()
        .parse("{% for i in (1..100) %}{{ abc }}{% endfor %}")
        .assertRenderResult(
            data = "{\"abc\": \"abcdefghijklmnopqrstuvwxyz\"}",
            renderer = { maxSizeRenderedString(2700) })
        .isNotError()
  }

  @Test
  fun testExceedMaxSizeRenderedString() {
    TemplateFactory.newBuilder()

        .parse("{% for i in (1..1000) %}{{ abc }}{% endfor %}")
        .assertRenderResult(
            data = "{\"abc\": \"abcdefghijklmnopqrstuvwxyz\"}",
            renderer = {
              maxSizeRenderedString(2500)
            })
        .isRenderError()
  }
}
