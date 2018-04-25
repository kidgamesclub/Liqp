package liqp.nodes;

import liqp.node.LNode;
import lombok.Getter;

@Getter
public class OrNode extends ExpressionNode {

  public OrNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
  public Object render(LContext context) {
    return context.isTrue(lhs.render(context)) || context.isTrue(rhs.render(context));
  }
}
