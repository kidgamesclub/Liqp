package liqp.filters;

import static java.util.Collections.emptyMap;

import liqp.TemplateEngine;
import liqp.TemplateFactory;
import liqp.nodes.RenderContext;

public class Mocks {
  public static RenderContext mockRenderContext() {
    return new RenderContext(emptyMap(),
          new TemplateFactory(),
          new TemplateEngine());
  }
}
