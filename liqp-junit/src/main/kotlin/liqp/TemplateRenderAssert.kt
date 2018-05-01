package liqp

import liqp.context.LContext
import liqp.node.LTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.assertj.core.api.ListAssert

class TemplateRenderAssert(val template: LTemplate? = null,
                           private val renderResult: Any? = null,
                           private val context: LContext? = null,
                           val error: Exception? = null) {

  private val renderedString: String? by lazy {
    context!!.asString(renderResult)
  }

  fun isNotError(): TemplateRenderAssert {
    assertThat(error).isNull()
    return this
  }

  fun isNullResult(): TemplateRenderAssert {
    assertThat(renderResult).isNull()
    return this
  }

  fun isNotNullResult(): TemplateRenderAssert {
    assertThat(renderResult).isNotNull
    return this
  }

  fun isString(): TemplateRenderAssert {
    isNotNullResult()
    renderResult!!
    assertThat(renderResult)
        .describedAs("Result should have been a string, but was ${renderResult::class.java}")
        .isInstanceOf(String::class.java)
    return this
  }

  fun isTruncated(limit: Int = -1): TemplateRenderAssert {
    isNotError().isNotNullResult().isString()

    assertThat(renderResult as String).describedAs("Should have been truncated").endsWith("...")
    if (limit > -1) {
      assertThat(renderResult).describedAs("Should be truncated to $limit chars").hasSize(limit)
    }

    return this
  }

  fun hasRenderError(type: Class<out Throwable> = Throwable::class.java): TemplateRenderAssert {
    if (template == null && error != null) {
      fail("There was a failure, but it happened during parsing $error")
    }
    assertThat(error).`as`("Should have thrown error but didn't Rendered: %s", this.renderResult)
        .isNotNull()
        .isInstanceOf(type)
    return this
  }

  fun isParseError(type: Class<out Throwable> = Throwable::class.java): TemplateRenderAssert {
    assertThat(template).`as`("There was an error, but not during parse").isNull()
    assertThat(error).isNotNull().isInstanceOf(type)
    return this
  }

  fun isEqualTo(value: Any): TemplateRenderAssert {
    isNotError()
    when (value) {
      is String -> assertThat(renderedString).isEqualTo(value)
      else -> assertThat(renderResult).isNotNull.isEqualTo(value)
    }
    return this
  }

  fun contains(value: String): TemplateRenderAssert {
    assertThat(renderedString).contains(value)
    return this
  }

  fun doesNotContain(value: String): TemplateRenderAssert {
    assertThat(renderedString).doesNotContain(value)
    return this
  }

  fun asList(): ListAssert<Any?> {
    isNotError().isNotNullResult()
    return assertThat(renderResult as List<Any?>)
  }

  fun containsExactly(vararg elements: Any) {
    asList().containsExactly(*elements)
  }
}
