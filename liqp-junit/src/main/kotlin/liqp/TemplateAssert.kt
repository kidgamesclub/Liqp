package liqp

import liqp.node.LTemplate
import org.assertj.core.api.Assertions.assertThat

class TemplateAssert(val template: LTemplate? = null,
                     private val engine:LiquidRenderer = LiquidRenderer.newInstance(),
                     val error:Exception? = null) {

  fun parsedWithoutError():TemplateAssert {
    assertThat(error).isNull()
    return this
  }

  fun parsedWithError(type: Class<in Throwable> = Throwable::class.java): TemplateAssert {
    assertThat(error).isNotNull()
        .isInstanceOf(type)
    return this
  }

  fun withEngine(configurer: RenderConfigurer): TemplateAssert {
    return TemplateAssert(template, engine.withRenderSettings(configurer))
  }

  @JvmOverloads fun rendering(inputData:Any? = null): TemplateRenderAssert {
    return renderAssert(template!!, engine, inputData)
  }
}
