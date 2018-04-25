package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;
import liqp.context.LContext;

public class Break extends LTag {
  @Override
  public Object render(LContext context, LNode... nodes) {
    return BREAK;
  }
}
