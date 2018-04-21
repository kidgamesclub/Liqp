package liqp.filters;

import liqp.TruthKt;
import liqp.nodes.RenderContext;

public class Default extends Filter {

  @Override
  public Object apply(RenderContext context, Object value, Object... params) {

    if (params == null || params.length == 0) {
      return value;
    }

    if (TruthKt.isFalsy(value)) {
      return params[0];
    }

    return value;
  }
}
