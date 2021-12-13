package liqp

import java.util.Collections.emptyMap

import liqp.LiquidRenderer
import liqp.LiquidParser
import liqp.nodes.RenderContext

class Mocks {
  companion object {
    @JvmStatic
    fun mockRenderContext(): RenderContext {
      val parser = provider.createParser()

      return RenderContext(emptyMap<Any, Any>(),
          logic = StrictLogic(),
          parser = parser,
          renderer = parser.toRenderer())
    }
  }

}
