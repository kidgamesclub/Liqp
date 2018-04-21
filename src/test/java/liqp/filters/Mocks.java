package liqp.filters;

import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.mock;

import liqp.TemplateEngine;
import liqp.TemplateFactory;
import liqp.nodes.RenderContext;

public class Mocks {
  public static RenderContext mockRenderContext() {
    return new RenderContext(emptyMap(),
          mock(TemplateFactory.class),
          mock(TemplateEngine.class));
  }
}
