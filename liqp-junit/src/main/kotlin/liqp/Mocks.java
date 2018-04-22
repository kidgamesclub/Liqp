package liqp;

import static java.util.Collections.emptyMap;

import liqp.LiquidRenderer;
import liqp.LiquidParser;
import liqp.nodes.RenderContext;

public class Mocks {
  public static RenderContext mockRenderContext() {
    return new RenderContext(emptyMap(),
          new LiquidParser(),
          new LiquidRenderer());
  }
}
