package liqp

import java.util.Collections.emptyMap

import liqp.LiquidRenderer
import liqp.LiquidParser
import liqp.nodes.RenderContext

class Mocks {
  companion object {
    @JvmStatic
    fun mockRenderContext(): RenderContext {
      return RenderContext(emptyMap<Any, Any>(),
          logic = strictLogic,
          parser = LiquidParser(),
          renderer = LiquidRenderer())
    }
  }

}
