package liqp

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

class TemplateRenderAssert(val template: Template? = null, val renderResult: Any? = null,
                           val error: Exception? = null) {

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

  fun isTruncated(limit:Int=-1):TemplateRenderAssert {
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
    when (value) {
      is String -> assertThat(renderResult.toNonNullString()).isEqualTo(value)
      else -> assertThat(renderResult).isNotNull.isEqualTo(value)
    }
    return this
  }

  fun contains(value: String): TemplateRenderAssert {
    assertThat(renderResult.toNonNullString()).contains(value)
    return this
  }

  fun doesNotContain(value: String): TemplateRenderAssert {
    assertThat(renderResult.toNonNullString()).doesNotContain(value)
    return this
  }
}
