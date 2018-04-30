package liqp

import liqp.config.MutableRenderSettings
import liqp.config.RenderSettings
import liqp.context.LContext
import liqp.node.LTemplate
import java.util.concurrent.ExecutorService

interface LRenderer {
  val settings: RenderSettings
  fun withParser(parser: LParser): LRenderer
  fun withExecutorService(executor: ExecutorService): LRenderer
  fun withRenderSettings(settings: RenderSettings): LRenderer
  fun withRenderSettings(callback: (MutableRenderSettings) -> MutableRenderSettings): LRenderer
  fun createRenderContext(inputData: Any?): LContext
  fun execute(template: LTemplate, inputData: Any? = null): Any?
  fun executeWithContext(template: LTemplate, context: LContext): Any?
  fun render(template: LTemplate, context: LContext): String
  fun render(template: LTemplate, inputData: Any? = null): String
  fun getAccessor(prototype:Any, prop:String):Getter<Any>
}
